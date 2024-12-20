package com.bjcareer.GPTService.out.api.gpt.news;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.GPTService.config.gpt.GPTWebConfig;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.QuestionPrompt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTNewsAdapter {
	public static final String MODEL = "ft:gpt-4o-mini-2024-07-18:personal::AdspZUDm";
	public static final String GPT_4o = "gpt-4o";
	private final WebClient webClient;

	//가장 좋은 모델을 선택해서 테스트 케이스 구축
	public Optional<GPTNewsDomain> findStockRaiseReason(OriginalNews originalNews, String stockName, LocalDate pubDate) {
		ClientResponse response = sendAnalyzeStockNews(originalNews, stockName, pubDate, MODEL);

		log.info("Request with News link: {}", originalNews.getNewsLink());
		Optional<GPTNewsResponseDTO.Content> res = parseContent(response);
		if(res.isEmpty()){
			log.warn("Failed to parse content: {}", response);
			return Optional.empty();
		}

		GPTNewsResponseDTO.Content content = res.get();
		log.info("Request successed with News link: {}", originalNews.getNewsLink());

		if (content.isRelevant()) {
			log.info("결과가 성공이라고 나와서 검증 시도: {}", content);
			GPTNewsResponseDTO.Content finalContent = content;
			response = sendAnalyzeStockNews(originalNews, stockName, pubDate, GPT_4o);
			content = parseContent(response).orElseGet(() -> {
				log.warn("Failed to parse content: {}");
				return finalContent;
			});
		}

		return Optional.of(
				new GPTNewsDomain(content.getName(), content.getReason(), content.getNext(),
					content.getNextReason().getFact(), content.getNextReason().getOpinion(), originalNews,
					content.isRelevant(), content.getIsRelevantDetail(), content.isThema(), content.getKeywords()));
		}

	private Optional<GPTNewsResponseDTO.Content> parseContent(ClientResponse response) {
		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTNewsResponseDTO gptNewsResponseDTO = handleSuccessResponse(response);
			GPTNewsResponseDTO.Content parsedContent = gptNewsResponseDTO.getChoices()
				.get(0)
				.getMessage()
				.getParsedContent();

			if (parsedContent == null) {
				log.warn("Parsed content is null");
				return Optional.empty();
			}

			if (!parsedContent.isRelevant()) {
				log.warn("Wrong Parsed content: {}", parsedContent);
			}

			return Optional.of(parsedContent);
		}

		return Optional.empty();
	}

	private ClientResponse sendAnalyzeStockNews(OriginalNews originalNews, String stockName, LocalDate pubDate,
		String model) {
		log.debug("Requesting GPT with news with model: {}", model);
		GPTNewsRequestDTO requestDTO = createRequestDTO(originalNews.getContent(), originalNews.getTitle(), stockName,
			pubDate, model);
		ClientResponse response = sendRequestToGPT(requestDTO).block();
		return response;
	}

	private GPTNewsRequestDTO createRequestDTO(String content, String title, String name,
		LocalDate pubDate, String model) {
		GPTNewsRequestDTO.Message systemMessage = new GPTNewsRequestDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + GPTWebConfig.SYSTEM_THEMA_TEXT);

		GPTNewsRequestDTO.Message userMessage = new GPTNewsRequestDTO.Message(GPTWebConfig.USER_ROLE,
		QuestionPrompt.QUESTION_FORMAT.formatted(pubDate, name, title, content));

		GPTResponseNewsFormatDTO gptResponseNewsFormatDTO = new GPTResponseNewsFormatDTO();
		return new GPTNewsRequestDTO(model, List.of(systemMessage, userMessage), gptResponseNewsFormatDTO);
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTNewsRequestDTO requestDTO) {
		return webClient.post()
			.uri(GPTWebConfig.URI)
			.bodyValue(requestDTO)
			.exchange();
	}

	private GPTNewsResponseDTO handleSuccessResponse(ClientResponse response) {
		return response.bodyToMono(GPTNewsResponseDTO.class).block();
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
