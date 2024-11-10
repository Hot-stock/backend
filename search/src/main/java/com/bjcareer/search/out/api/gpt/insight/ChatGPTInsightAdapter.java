package com.bjcareer.search.out.api.gpt.insight;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.application.port.out.api.GPTInsightPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.config.gpt.GPTWebConfig;
import com.bjcareer.search.domain.GPTInsight;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTInsightAdapter implements GPTInsightPort {
	private final WebClient webClient;

	@Override
	public GPTInsight getInsight(List<GTPNewsDomain> newes, LocalDate baseDate) {
		GPTRequestInsightDTO requestDTO = createRequestDTO(newes);

		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTResponseInsightDTO gptResponseInsightDTO = handleSuccessResponse(response);
			GPTResponseInsightDTO.Content parsedContent = gptResponseInsightDTO.getChoices()
				.getFirst()
				.getMessage()
				.getParsedContent();

			return new GPTInsight();
		} else {
			handleErrorResponse(response);
			return null;
		}
	}

	private GPTRequestInsightDTO createRequestDTO(List<GTPNewsDomain> newes) {
		String question = createDomainToJson(newes);

		GPTRequestInsightDTO.Message systemMessage = new GPTRequestInsightDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT);
		GPTRequestInsightDTO.Message userMessage = new GPTRequestInsightDTO.Message(GPTWebConfig.USER_ROLE,
			question + "반듯이 한글로 답변");
		GPTResponseInsightFormatDTO gptResponseInsightFormatDTO = new GPTResponseInsightFormatDTO();

		return new GPTRequestInsightDTO(GPTWebConfig.MODEL, List.of(systemMessage, userMessage),
			gptResponseInsightFormatDTO);
	}

	private String createDomainToJson(List<GTPNewsDomain> newes) {
		ObjectMapper objectMapper = AppConfig.customObjectMapper();
		StringBuilder reason = new StringBuilder();
		StringBuilder nextReason = new StringBuilder();

		for (GTPNewsDomain gptNews : newes) {
			String upReason =
				gptNews.getNews().getPubDate() + " " + gptNews.getReason() + " " + gptNews.getNews().getOriginalLink();
			reason.append(upReason).append("\n");

			if (gptNews.getNext().isEmpty()) {
				continue;
			}

			if (gptNews.getNext().get().isAfter(LocalDate.now())) {
				String nextUpReason =
					gptNews.getNext().get() + " " + gptNews.getNextReason() + " " + gptNews.getNews().getOriginalLink();
				nextReason.append(nextUpReason).append("\n");
			}
		}

		Map<String, String> map = Map.of("reason", reason.toString(), "nextReason", nextReason.toString());

		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTRequestInsightDTO requestDTO) {
		return webClient.post()
			.uri(GPTWebConfig.URI)
			.bodyValue(requestDTO)
			.exchange();
	}

	private GPTResponseInsightDTO handleSuccessResponse(ClientResponse response) {
		// 동기적으로 body를 읽음
		GPTResponseInsightDTO gptResponse = response.bodyToMono(GPTResponseInsightDTO.class).block();
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
