package com.bjcareer.search.out.api.gpt.thema;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.config.gpt.GPTWebConfig;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.gpt.thema.GPTThema;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTThemaAdapter {
	public static final String THEMA_MODEL = "ft:gpt-4o-mini-2024-07-18:personal::AVZSfNt0";
	private final WebClient webClient;

	public Optional<GPTThema> summaryThemaNews(News news, String name) {
		GPTThemaRequestDTO requestDTO = createRequestDTO(news.getContent(), name, news.getPubDate());

		// 동기적으로 요청을 보내고 결과를 block()으로 기다림
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTThemaResponseDTO gptThemaResponseDTO = handleSuccessResponse(response);
			GPTThemaResponseDTO.Content parsedContent = gptThemaResponseDTO.getChoices()
				.getFirst()
				.getMessage()
				.getParsedContent();

			if (!parsedContent.isRelatedThema()) {
				log.warn("The response is not related to the topic.");
			}

			return Optional.of(new GPTThema(parsedContent.isRelatedThema(), parsedContent.getSummary(), parsedContent.getUpcomingDate(),
				parsedContent.getUpcomingDateReason(), parsedContent.getHistoryPattern(), news));
		} else {
			handleErrorResponse(response);
			return Optional.empty();
		}
	}

	private GPTThemaRequestDTO createRequestDTO(String message, String name, LocalDate pubDate) {
		GPTThemaRequestDTO.Message systemMessage = new GPTThemaRequestDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + "테마주 뉴스를 분석해줘");
		GPTThemaRequestDTO.Message userMessage = new GPTThemaRequestDTO.Message(
			GPTWebConfig.USER_ROLE, ThemaQuestionPrompt.QUESTION_PROMPT.formatted(pubDate, name, message));

		GPTResponseThemaFormatDTO gptResponseThemaFormatDTO = new GPTResponseThemaFormatDTO();

		return new GPTThemaRequestDTO(THEMA_MODEL, List.of(systemMessage, userMessage), gptResponseThemaFormatDTO);
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTThemaRequestDTO requestDTO) {
		return webClient.post().uri(GPTWebConfig.URI).bodyValue(requestDTO).exchange();
	}

	private GPTThemaResponseDTO handleSuccessResponse(ClientResponse response) {
		// 동기적으로 body를 읽음
		GPTThemaResponseDTO gptResponse = response.bodyToMono(GPTThemaResponseDTO.class).block();
		log.info("Response body: {}", gptResponse);
		return gptResponse;
	}

	private void handleErrorResponse(ClientResponse response) {
		if (response != null) {
			HttpStatusCode statusCode = response.statusCode();
			log.error("Request failed with status code: {}", statusCode);
			log.error("Error body: {}", response.bodyToMono(String.class).block());
		} else {
			log.error("Request failed: response is null.");
		}
	}
}
