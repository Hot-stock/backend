package com.bjcareer.search.out.api.gpt;

import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

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
	private static final String SYSTEM_MESSAGE_TEXT = "You are a stock analyst. Your task is to summarize news articles, identify the next key event date for the stock, analyze the reasons for its rise, and determine which thematic category the stock belongs to.";
	private static final String USER_MESSAGE_TEXT = "Read this news article and explain using Korean.";

	private final WebClient webClient;

	public GPTResponseDTO getStockReason(String stockLink) {
		GPTRequestDTO requestDTO = createRequestDTO(stockLink);

		// 동기적으로 요청을 보내고 결과를 block()으로 기다림
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			return handleSuccessResponse(response);
		} else {
			handleErrorResponse(response);
			return null; // 실패 시 null 반환 또는 예외 처리
		}
	}

	private GPTRequestDTO createRequestDTO(String stockLink) {
		GPTRequestDTO.Message systemMessage = new GPTRequestDTO.Message(SYSTEM_ROLE, SYSTEM_MESSAGE_TEXT);
		GPTRequestDTO.Message userMessage = new GPTRequestDTO.Message(USER_ROLE, USER_MESSAGE_TEXT);
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
