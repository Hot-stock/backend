package com.bjcareer.search.out.api.gpt.news;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.application.port.out.api.GPTNewsPort;
import com.bjcareer.search.config.gpt.GPTWebConfig;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTNewsAdapter implements GPTNewsPort {
	private final WebClient webClient;

	@Override
	public Optional<GTPNewsDomain> findStockRaiseReason(String message, String name, LocalDate pubDate) {
		GPTNewsRequestDTO requestDTO = createRequestDTO(message, name, pubDate);

		// 동기적으로 요청을 보내고 결과를 block()으로 기다림
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTNewsResponseDTO gptNewsResponseDTO = handleSuccessResponse(response);
			GPTNewsResponseDTO.Content parsedContent = gptNewsResponseDTO.getChoices()
				.getFirst()
				.getMessage()
				.getParsedContent();

			if (parsedContent.isFiltered()) {
				return Optional.empty();
			}

			Map<String, String> themas = new HashMap<>();

			for (ThemaVariableResponseDTO thema : parsedContent.getThemas()) {
				themas.put(thema.name, thema.reason);
			}

			return Optional.of(
				new GTPNewsDomain(parsedContent.getName(), parsedContent.getReason(), themas,
					parsedContent.getNext(), parsedContent.getNextReason()));
		} else {
			handleErrorResponse(response);
			return Optional.empty(); // 실패 시 null 반환 또는 예외 처리
		}
	}

	private GPTNewsRequestDTO createRequestDTO(String message, String name, LocalDate pubDate) {
		GPTNewsRequestDTO.Message systemMessage = new GPTNewsRequestDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT);
		GPTNewsRequestDTO.Message userMessage = new GPTNewsRequestDTO.Message(GPTWebConfig.USER_ROLE,
			"Today’s date is the news publication date: " + pubDate.toString() + ". Based on " + name
				+ ", analyze the following message: " + message + ". Provide the response in Korean.");

		GPTResponseNewsFormatDTO gptResponseNewsFormatDTO = new GPTResponseNewsFormatDTO();

		return new GPTNewsRequestDTO(GPTWebConfig.MODEL, List.of(systemMessage, userMessage), gptResponseNewsFormatDTO);
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTNewsRequestDTO requestDTO) {
		return webClient.post()
			.uri(GPTWebConfig.URI)
			.bodyValue(requestDTO)
			.exchange();
	}

	private GPTNewsResponseDTO handleSuccessResponse(ClientResponse response) {
		// 동기적으로 body를 읽음
		GPTNewsResponseDTO gptResponse = response.bodyToMono(GPTNewsResponseDTO.class).block();
		log.debug("GPT response: {}", gptResponse);
		return gptResponse;
	}

	private void handleErrorResponse(ClientResponse response) {
		if (response != null) {
			HttpStatusCode statusCode = response.statusCode();
			log.error("Request failed with status code: {}", statusCode);

			// 에러 메시지를 동기적으로 읽음
			String errorBody = response.bodyToMono(String.class).block();
			log.error("Error body: {}", errorBody);
		} else {
			log.error("Request failed: response is null.");
		}
	}
}
