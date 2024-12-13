package com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.GPTService.config.gpt.GPTWebConfig;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.gpt.thema.prompt.ThemaQuestionPrompt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTThemaAdapter {
	public static final String MODEL = "ft:gpt-4o-mini-2024-07-18:personal::AYTYXzqH";
	private final WebClient webClient;

	public Optional<GPTThema> summaryThemaNews(OriginalNews news, String knownThema) {
		GPTThemaRequestDTO requestDTO = createRequestDTO(news.getContent(), news.getPubDate(), knownThema);

		// // 동기적으로 요청을 보내고 결과를 block()으로 기다림
		// log.debug("News link: {}", news.getNewsLink());
		// ClientResponse response = sendRequestToGPT(requestDTO).block();
		//
		// if (response != null && response.statusCode().is2xxSuccessful()) {
		// 	GPTThemaResponseDTO gptThemaResponseDTO = handleSuccessResponse(response);
		// 	GPTThemaResponseDTO.Content parsedContent = gptThemaResponseDTO.getChoices()
		// 		.get(0)
		// 		.getMessage()
		// 		.getParsedContent();
		//
		// 	if (!parsedContent.isRealNews()) {
		// 		log.warn("The response is not related to the topic.");
		// 	}
		//
		// 	ThemaInfo themaInfo = new ThemaInfo(parsedContent.getThema().getStockName(),
		// 		parsedContent.getThema().getName(),
		// 		parsedContent.getThema().getReason());
		//
		// 	ThemaInfo oppositeThemaInfo = new ThemaInfo(parsedContent.getThema().getStockName(),
		// 		parsedContent.getOppositeThema().getName(),
		// 		parsedContent.getOppositeThema().getReason());
		//
		// 	return Optional.of(
		// 		new GPTThema(parsedContent.isRealNews(), parsedContent.isPositive(), parsedContent.getSummary(),
		// 			parsedContent.getUpcomingDate(), parsedContent.getUpcomingDateReason().getFact(),
		// 			parsedContent.getUpcomingDateReason().getOpinion(), news, themaInfo, oppositeThemaInfo));
		// } else {
		// 	handleErrorResponse(response);
		// 	return Optional.empty();
		// }

		return Optional.empty();
	}

	private GPTThemaRequestDTO createRequestDTO(String content, LocalDate pubDate, String knownThema) {
		GPTThemaRequestDTO.Message systemMessage = new GPTThemaRequestDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + GPTWebConfig.SYSTEM_THEMA_TEXT);

		GPTThemaRequestDTO.Message userMessage = new GPTThemaRequestDTO.Message(
			GPTWebConfig.USER_ROLE, ThemaQuestionPrompt.QUESTION_PROMPT.formatted(pubDate, content, knownThema));

		GPTResponseThemaFormatDTO gptResponseThemaFormatDTO = new GPTResponseThemaFormatDTO();

		return new GPTThemaRequestDTO(MODEL, List.of(systemMessage, userMessage), gptResponseThemaFormatDTO);
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTThemaRequestDTO requestDTO) {
		return webClient.post().uri(GPTWebConfig.URI).bodyValue(requestDTO).exchange();
	}

	private GPTThemaResponseDTO handleSuccessResponse(ClientResponse response) {
		// 동기적으로 body를 읽음
		GPTThemaResponseDTO gptResponse = response.bodyToMono(GPTThemaResponseDTO.class).block();
		log.debug("Response body: {}", gptResponse);
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
