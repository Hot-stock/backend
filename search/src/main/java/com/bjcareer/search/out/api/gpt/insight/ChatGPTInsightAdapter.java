package com.bjcareer.search.out.api.gpt.insight;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.application.port.out.api.GPTInsightPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.config.gpt.GPTWebConfig;
import com.bjcareer.search.domain.GPTInsight;
import com.bjcareer.search.domain.GPTThema;
import com.bjcareer.search.domain.GTPNewsDomain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTInsightAdapter implements GPTInsightPort {
	private final WebClient webClient;

	@Override
	public GPTInsight getInsight(List<GTPNewsDomain> newes, List<GPTThema> themas, LocalDate baseDate) {
		GPTRequestInsightDTO requestDTO = createRequestDTO(newes, themas);

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

	private GPTRequestInsightDTO createRequestDTO(List<GTPNewsDomain> newes, List<GPTThema> themas) {
		StringBuilder domainToJson = createDomainToJson(newes);
		domainToJson.append(createThemaDomainToJson(themas));

		GPTRequestInsightDTO.Message systemMessage = new GPTRequestInsightDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + "넌 슈퍼개미 시간여행님의 통찰력을 가지고 있어");
		GPTRequestInsightDTO.Message userMessage = new GPTRequestInsightDTO.Message(GPTWebConfig.USER_ROLE,
			"오늘 날짜는" + LocalDate.now(AppConfig.ZONE_ID) + domainToJson + "반듯이 한글로 답변");
		GPTResponseInsightFormatDTO gptResponseInsightFormatDTO = new GPTResponseInsightFormatDTO();

		return new GPTRequestInsightDTO("gpt-4o", List.of(systemMessage, userMessage),
			gptResponseInsightFormatDTO);
	}

	private StringBuilder createDomainToJson(List<GTPNewsDomain> newes) {
		StringBuilder reason = new StringBuilder();
		StringBuilder nextReason = new StringBuilder();

		reason.append("그동안 7%이상 상승한 날들에 대한 이유를 요약해봤어\n");
		nextReason.append("뉴스에 나온 예상된 일자들을 요약을 해봤어\n");

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

		return reason.append(nextReason);
	}

	private StringBuilder createThemaDomainToJson(List<GPTThema> newes) {
		StringBuilder sb = new StringBuilder();
		sb.append("이 주식이 속한 테마들의 뉴스들을 요약해봤어\n");

		for (GPTThema gptThema : newes) {
			sb.append(gptThema.getSummary()).append("\n");
			sb.append(gptThema.getCatalysts()).append("\n");
			sb.append(gptThema.getPolicyImpactAnalysis()).append("\n");
			sb.append(gptThema.getRecoveryProjectDetails()).append("\n");
			sb.append(gptThema.getInterestRateImpact()).append("\n");
			sb.append(gptThema.getMarketOutlook()).append("\n");
			sb.append(gptThema.getScenarioAnalysis()).append("\n");
			sb.append(gptThema.getKeyUpcomingDates()).append("\n");
			sb.append(gptThema.getInvestmentAttractiveness()).append("\n");
		}

		return sb;
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
