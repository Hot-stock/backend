package com.bjcareer.search.out.api.gpt;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.domain.GTPNewsDomain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPT {
	private static final String MODEL = "gpt-4o";
	private static final String URI = "/chat/completions";
	private static final String SYSTEM_ROLE = "system";
	private static final String USER_ROLE = "user";
	private static final String SYSTEM_MESSAGE_TEXT = "You are a stock analyst. Your task is to summarize news articles, identify the next key event date for the stock, analyze the reasons for its rise, and determine which thematic category the stock belongs to. 반듯이 한글로 답변해";

	private final WebClient webClient;

	public Optional<GTPNewsDomain> getStockReason(String message, String name) {
		GPTRequestDTO requestDTO = createRequestDTO(message, name);

		// 동기적으로 요청을 보내고 결과를 block()으로 기다림
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTResponseDTO gptResponseDTO = handleSuccessResponse(response);
			GPTResponseDTO.Content parsedContent = gptResponseDTO.getChoices()
				.getFirst()
				.getMessage()
				.getParsedContent();

			return Optional.of(
				new GTPNewsDomain(parsedContent.getName(), parsedContent.getReason(), parsedContent.getThema(),
					parsedContent.getNext(), parsedContent.getNextReason()));
		} else {
			handleErrorResponse(response);
			return Optional.empty(); // 실패 시 null 반환 또는 예외 처리
		}
	}

	private GPTRequestDTO createRequestDTO(String message, String name) {
		GPTRequestDTO.Message systemMessage = new GPTRequestDTO.Message(SYSTEM_ROLE, SYSTEM_MESSAGE_TEXT);
		GPTRequestDTO.Message userMessage = new GPTRequestDTO.Message(USER_ROLE, name + "위주로 분석 " + message);
		GPTResponseFormatDTO gptResponseFormatDTO = new GPTResponseFormatDTO();

		return new GPTRequestDTO(MODEL, List.of(systemMessage, userMessage), gptResponseFormatDTO);
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTRequestDTO requestDTO) {
		return webClient.post()
			.uri(URI)
			.bodyValue(requestDTO)
			.exchange();
	}

	private GPTResponseDTO handleSuccessResponse(ClientResponse response) {
		// 동기적으로 body를 읽음
		GPTResponseDTO gptResponse = response.bodyToMono(GPTResponseDTO.class).block();
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
