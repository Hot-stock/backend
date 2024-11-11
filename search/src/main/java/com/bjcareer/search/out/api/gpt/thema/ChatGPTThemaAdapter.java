package com.bjcareer.search.out.api.gpt.thema;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.config.gpt.GPTWebConfig;
import com.bjcareer.search.domain.gpt.thema.CatalystsVariableDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThema;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTThemaAdapter {
	private final WebClient webClient;

	public GPTThema summaryThemaNews(String message, String name, LocalDate pubDate) {
		GPTThemaRequestDTO requestDTO = createRequestDTO(message, name, pubDate);

		// 동기적으로 요청을 보내고 결과를 block()으로 기다림
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTThemaResponseDTO gptThemaResponseDTO = handleSuccessResponse(response);
			GPTThemaResponseDTO.Content parsedContent = gptThemaResponseDTO.getChoices()
				.getFirst()
				.getMessage()
				.getParsedContent();

			List<CatalystsVariableDomain> catalystsVariableDomains = parsedContent.getCatalysts()
				.stream()
				.map(catalyst -> new CatalystsVariableDomain(catalyst.keyword, catalyst.catalyst))
				.toList();

			return new GPTThema(parsedContent.getSummary(), catalystsVariableDomains,
				parsedContent.getPolicyImpactAnalysis(), parsedContent.getRecoveryProjectDetails(),
				parsedContent.getInterestRateImpact(), parsedContent.getMarketOutlook(),
				parsedContent.getScenarioAnalysis(), parsedContent.getKeyUpcomingDates(),
				parsedContent.getInvestmentAttractiveness());
		} else {
			handleErrorResponse(response);
			; // 실패 시 null 반환 또는 예외 처리
			return null;
		}
	}

	private GPTThemaRequestDTO createRequestDTO(String message, String name, LocalDate pubDate) {
		GPTThemaRequestDTO.Message systemMessage = new GPTThemaRequestDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT);
		GPTThemaRequestDTO.Message userMessage = new GPTThemaRequestDTO.Message(GPTWebConfig.USER_ROLE,
			"The publication date of this news is " + pubDate.toString() + ". Today's date is " + LocalDate.now(
				AppConfig.ZONE_ID) + ", based on " + name + ". Summarize the following message: " + message
				+ " and respond in Korean.");
		GPTResponseThemaFormatDTO gptResponseThemaFormatDTO = new GPTResponseThemaFormatDTO();

		return new GPTThemaRequestDTO(GPTWebConfig.MODEL, List.of(systemMessage, userMessage),
			gptResponseThemaFormatDTO);
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTThemaRequestDTO requestDTO) {
		return webClient.post().uri(GPTWebConfig.URI).bodyValue(requestDTO).exchange();
	}

	private GPTThemaResponseDTO handleSuccessResponse(ClientResponse response) {
		// 동기적으로 body를 읽음
		GPTThemaResponseDTO gptResponse = response.bodyToMono(GPTThemaResponseDTO.class).block();
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
