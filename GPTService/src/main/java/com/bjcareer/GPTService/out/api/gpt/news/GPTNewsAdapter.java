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
	public static final String MODEL = "ft:gpt-4o-mini-2024-07-18:personal::AcOoOpPg";
	private final WebClient webClient;

	//가장 좋은 모델을 선택해서 테스트 케이스 구축
	public Optional<GPTNewsDomain> findStockRaiseReason(OriginalNews originalNews, String stockName, LocalDate pubDate) {
		GPTNewsRequestDTO requestDTO = createRequestDTO(originalNews.getContent(), stockName, pubDate);

		// 동기적으로 요청을 보내고 결과를 block()으로 기다림
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			log.info("Request succeeded with News link: {}", originalNews.getNewsLink());
			GPTNewsResponseDTO gptNewsResponseDTO = handleSuccessResponse(response);
			GPTNewsResponseDTO.Content parsedContent = gptNewsResponseDTO.getChoices()
				.get(0)
				.getMessage()
				.getParsedContent();

			if(parsedContent == null) {
				log.warn("Parsed content is null");
				return Optional.empty();
			}

			if (!parsedContent.isRelevant()) {
				log.warn("Wrong Parsed content: {}", parsedContent);
			}

			return Optional.of(
				new GPTNewsDomain(parsedContent.getName(), parsedContent.getReason(), parsedContent.getNext(),
					parsedContent.getNextReason().getFact(), parsedContent.getNextReason().getOpinion(), originalNews,
					parsedContent.isRelevant()));
		} else {
			handleErrorResponse(response);
			return Optional.empty(); // 실패 시 null 반환 또는 예외 처리
		}
	}

	private GPTNewsRequestDTO createRequestDTO(String message, String name,
		LocalDate pubDate) {
		GPTNewsRequestDTO.Message systemMessage = new GPTNewsRequestDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + "뉴스를 분석해줘");

		GPTNewsRequestDTO.Message userMessage = new GPTNewsRequestDTO.Message(GPTWebConfig.USER_ROLE,
			QuestionPrompt.QUESTION_FORMAT.formatted(pubDate, name, message));

		GPTResponseNewsFormatDTO gptResponseNewsFormatDTO = new GPTResponseNewsFormatDTO();

		return new GPTNewsRequestDTO(MODEL, List.of(systemMessage, userMessage), gptResponseNewsFormatDTO);
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
