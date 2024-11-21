package com.bjcareer.search.out.api.gpt.insight;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.application.port.out.api.GPTInsightPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.config.gpt.GPTWebConfig;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;
import com.bjcareer.search.domain.gpt.insight.BuyRecommendationVariableDomain;
import com.bjcareer.search.domain.gpt.insight.GPTInsight;
import com.bjcareer.search.domain.gpt.insight.KeyDateVariableDomain;
import com.bjcareer.search.domain.gpt.insight.LongTermThesisVariableDomain;
import com.bjcareer.search.domain.gpt.insight.ShortTermStrategyVariableDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThema;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTInsightAdapter implements GPTInsightPort {
	private final WebClient webClient;

	@Override
	public GPTInsight getInsight(List<GTPNewsDomain> newsList, List<GPTThema> themas, LocalDate baseDate) {
		GPTRequestInsightDTO requestDTO = createRequestDTO(newsList, themas);
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
			.getFirst()
			.getMessage()
			.getParsedContent();

		BuyRecommendationVariableDomain buyRecommendation = createBuyRecommendation(parsedContent);
		List<KeyDateVariableDomain> keyDates = createKeyDates(parsedContent);
		List<ShortTermStrategyVariableDomain> shortTermStrategies = createShortTermStrategies(parsedContent);
		List<LongTermThesisVariableDomain> longTermTheses = createLongTermTheses(parsedContent);

		return new GPTInsight(buyRecommendation, parsedContent.getMarketDrivers(), keyDates, shortTermStrategies,
			longTermTheses,
			parsedContent.getVolumeTargetForGain(), parsedContent.getRiskPeriods());
	}

	private BuyRecommendationVariableDomain createBuyRecommendation(GPTResponseInsightDTO.Content content) {
		return new BuyRecommendationVariableDomain(
			content.getBuyRecommendation().score, content.getBuyRecommendation().reason);
	}

	private List<KeyDateVariableDomain> createKeyDates(GPTResponseInsightDTO.Content content) {
		return content.getKeyDates().stream()
			.map(keyDate -> new KeyDateVariableDomain(keyDate.date, keyDate.reason))
			.collect(Collectors.toList());
	}

	private List<ShortTermStrategyVariableDomain> createShortTermStrategies(GPTResponseInsightDTO.Content content) {
		return content.getShortTermStrategy().stream()
			.map(strategy -> new ShortTermStrategyVariableDomain(
				strategy.date,
				strategy.catalystImpact,
				strategy.historicalImpact.stream()
					.map(detail -> new ShortTermStrategyVariableDomain.HistoricalImpactDetail(
						detail.impactDate, detail.reasonForRise, detail.sourceLink))
					.collect(Collectors.toList()),
				strategy.predictedImpact, strategy.probabilityOfIncrease, strategy.investorInsight))
			.collect(Collectors.toList());
	}

	private List<LongTermThesisVariableDomain> createLongTermTheses(GPTResponseInsightDTO.Content content) {
		return content.getLongTermThesis().stream()
			.map(thesis -> new LongTermThesisVariableDomain(
				thesis.historicalPatterns.stream()
					.map(pattern -> new LongTermThesisVariableDomain.HistoricalPatternDetail(
						pattern.eventDate, pattern.patternDescription,
						pattern.marketImpact, pattern.investorAction))
					.collect(Collectors.toList()),
				thesis.catalystAnalysis.stream()
					.map(catalyst -> new LongTermThesisVariableDomain.CatalystDetail(
						catalyst.catalystDescription, catalyst.expectedImpact,
						catalyst.historicalComparison, catalyst.investorInsight))
					.collect(Collectors.toList()),
				thesis.riskResilienceFactors, thesis.projectedOutcomes))
			.collect(Collectors.toList());
	}

	private GPTRequestInsightDTO createRequestDTO(List<GTPNewsDomain> newsList, List<GPTThema> themas) {
		String domainJson = createDomainJson(newsList) + createThemaDomainJson(themas);
		GPTRequestInsightDTO.Message systemMessage = new GPTRequestInsightDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + "You possess the insights of a 'Super Ant Time Traveler'");
		GPTRequestInsightDTO.Message userMessage = new GPTRequestInsightDTO.Message(GPTWebConfig.USER_ROLE,
			"Today's date is " + LocalDate.now(AppConfig.ZONE_ID) + domainJson + "Please respond strictly in Korean");

		return new GPTRequestInsightDTO("gpt-4o", List.of(systemMessage, userMessage),
			new GPTResponseInsightFormatDTO());
	}

	private String createDomainJson(List<GTPNewsDomain> newsList) {
		StringBuilder result = new StringBuilder("그동안 7%이상 상승한 날들에 대한 이유를 요약해봤어\n");

		newsList.forEach(news -> {
			result.append(news.getNews().getPubDate()).append(" ").append(news.getReason()).append(" ")
				.append(news.getNews().getOriginalLink()).append("\n");
			news.getNext().ifPresent(nextDate -> {
				if (nextDate.isAfter(LocalDate.now())) {
					result.append(nextDate).append(" ").append(news.getNextReason()).append(" ")
						.append(news.getNews().getOriginalLink()).append("\n");
				}
			});
		});
		return result.toString();
	}

	private String createThemaDomainJson(List<GPTThema> themas) {
		StringBuilder result = new StringBuilder("이 주식이 속한 테마들의 뉴스들을 요약해봤어\n");
		themas.forEach(thema -> {
			result.append(thema.getSummary()).append("\n")
				.append(thema.getSummary()).append("\n")
				.append(thema.getUpcomingDate()).append("\n")
				.append(thema.getUpcomingDateReason()).append("\n");
		});
		return result.toString();
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
