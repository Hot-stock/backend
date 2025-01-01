package com.bjcareer.GPTService.out.api.gpt.insight.score;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.GPTService.config.AppConfig;
import com.bjcareer.GPTService.config.gpt.GPTWebConfig;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.GPTTriggerBackground;
import com.bjcareer.GPTService.domain.gpt.insight.GPTInsight;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.gpt.insight.score.dtos.GPTRequestInsightDTO;
import com.bjcareer.GPTService.out.api.gpt.insight.score.dtos.GPTResponseInsightDTO;
import com.bjcareer.GPTService.out.api.gpt.insight.score.dtos.GPTResponseInsightFormatDTO;
import com.bjcareer.GPTService.out.api.gpt.insight.score.dtos.NewsDTO;
import com.bjcareer.GPTService.out.api.gpt.insight.score.dtos.ThemaBackgroundDTO;
import com.bjcareer.GPTService.out.api.gpt.insight.score.prompt.QuestionPrompt;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTInsightAdapter {
	private final WebClient webClient;

	public GPTInsight getInsight(String stockName, List<GPTNewsDomain> raiseReason,
		List<GPTTriggerBackground> backgrounds,
		List<GPTThema> themaNews) {
		GPTRequestInsightDTO requestDTO = createRequestDTO(stockName, raiseReason, backgrounds, themaNews);
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			return processSuccessfulResponse(response);
		} else {
			handleErrorResponse(response);
			return null;
		}
	}

	private GPTInsight processSuccessfulResponse(ClientResponse response) {
		GPTResponseInsightDTO gptResponse = handleSuccessResponse(response);
		GPTResponseInsightDTO.Content parsedContent = gptResponse.getChoices()
			.get(0)
			.getMessage()
			.getParsedContent();

		return new GPTInsight(parsedContent.isFound(), parsedContent.getInsight().insight,
			parsedContent.getInsight().insightDetail,
			10);
	}

	private GPTRequestInsightDTO createRequestDTO(String stockName, List<GPTNewsDomain> raiseReason,
		List<GPTTriggerBackground> backgrounds, List<GPTThema> gptThemas) {

		String reason = changeReasonOfStockHistory(raiseReason);
		String background = changeBackgroundsToJsonFormat(backgrounds);
		String themaNews = changeGPTThemasToJsonFormat(gptThemas);

		GPTRequestInsightDTO.Message systemMessage = new GPTRequestInsightDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + "주가 과거 데아터와 트리거를 분석해서 제공된 기사를 보고 어떻게 연계할 수 있는지 근거와 배경을 설명해주세요.");

		GPTRequestInsightDTO.Message userMessage = new GPTRequestInsightDTO.Message(GPTWebConfig.USER_ROLE,
			QuestionPrompt.QUESTION_FORMAT.formatted(stockName, reason, background, themaNews, gptThemas.size()));

		return new GPTRequestInsightDTO("gpt-4o", List.of(systemMessage, userMessage),
			new GPTResponseInsightFormatDTO());
	}

	private String changeGPTThemasToJsonFormat(List<GPTThema> themas) {
		List<NewsDTO> list = themas.stream()
			.map(t -> new NewsDTO(t.getNews().getPubDate().toString(), t.getSummary(), t.getUpcomingDateReasonFact()))
			.toList();

		log.debug("Thema news list: {}", list.size());

		try {
			return AppConfig.customObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

	private String changeReasonOfStockHistory(List<GPTNewsDomain> newsList) {
		return newsList.stream().map(GPTNewsDomain::getReason).collect(Collectors.joining(","));
	}

	private String changeBackgroundsToJsonFormat(List<GPTTriggerBackground> backgrounds) {
		List<ThemaBackgroundDTO> list = backgrounds.stream()
			.map(t -> new ThemaBackgroundDTO(t.getThema(), t.getBackground(), t.getNextUseReason()))
			.toList();

		try {
			return AppConfig.customObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTRequestInsightDTO requestDTO) {
		return webClient.post()
			.uri(GPTWebConfig.URI)
			.bodyValue(requestDTO)
			.exchange();
	}

	private GPTResponseInsightDTO handleSuccessResponse(ClientResponse response) {
		GPTResponseInsightDTO gptResponse = response.bodyToMono(GPTResponseInsightDTO.class).block();
		log.debug("GPT response: {}", gptResponse);
		return gptResponse;
	}

	private void handleErrorResponse(ClientResponse response) {
		if (response != null) {
			log.error("Request failed with status code: {}", response.statusCode());
			String errorBody = response.bodyToMono(String.class).block();
			log.error("Error body: {}", errorBody);
		} else {
			log.error("Request failed: response is null.");
		}
	}
}
