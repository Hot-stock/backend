package com.bjcareer.search.out.api.gpt.news;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.application.port.out.api.GPTNewsPort;
import com.bjcareer.search.config.gpt.GPTWebConfig;
import com.bjcareer.search.domain.GTPNewsDomain;

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

			return Optional.of(
				new GTPNewsDomain(parsedContent.getName(), parsedContent.getReason(), parsedContent.getThema(),
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
			"오늘의 날짜는 뉴스 발행일은 " + pubDate.toString() + name + "을 기준으로 다음의 메세지를 분석하고" + message + "답변은 한글로");
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
