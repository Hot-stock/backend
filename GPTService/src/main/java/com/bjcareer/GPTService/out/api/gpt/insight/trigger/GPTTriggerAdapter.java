package com.bjcareer.GPTService.out.api.gpt.insight.trigger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.GPTService.config.gpt.GPTWebConfig;
import com.bjcareer.GPTService.domain.gpt.GPTTriggerBackground;
import com.bjcareer.GPTService.out.api.gpt.insight.trigger.prompt.QuestionPrompt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTTriggerAdapter {
	private final WebClient webClient;
	public final static String GPT_4o = "gpt-4o";

	public Optional<GPTTriggerBackground> getTrigger(List<String> raiseReasons, String thema, String model) {
		String summary = raiseReasons.stream().collect(Collectors.joining(","));

		GPTTriggerRequestDTO requestDTO = createRequestDTO(summary, thema, model);
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			return processSuccessfulResponse(response, thema);
		} else {
			handleErrorResponse(response);
			return Optional.empty();
		}
	}

	private Optional<GPTTriggerBackground> processSuccessfulResponse(ClientResponse response, String thema) {
		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTTriggerResponseDTO gptTriggerResponseDTO = handleSuccessResponse(response);
			GPTTriggerResponseDTO.Content parsedContent = gptTriggerResponseDTO.getChoices()
				.get(0)
				.getMessage()
				.getParsedContent();

			if (parsedContent == null) {
				log.warn("GPT error raise.");
				return Optional.empty();
			}

			GPTTriggerBackground gptTriggerBackground = new GPTTriggerBackground(thema, parsedContent.getNextUse(),
				parsedContent.getNextUseReason(), parsedContent.getBackground());
			gptTriggerBackground.addKeywords(parsedContent.getKeywords());

			return Optional.of(gptTriggerBackground);
		} else {
			handleErrorResponse(response);
			return Optional.empty();
		}
	}

	private GPTTriggerRequestDTO createRequestDTO(String summary, String thema, String model) {
		GPTTriggerRequestDTO.Message systemMessage = new GPTTriggerRequestDTO.Message(GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + GPTWebConfig.TRIGGER_SYSTEM_THEMA_TEXT);
		GPTTriggerRequestDTO.Message userMessage = new GPTTriggerRequestDTO.Message(GPTWebConfig.USER_ROLE,
			QuestionPrompt.QUESTION_FORMAT.formatted(thema, summary));

		return new GPTTriggerRequestDTO(model, List.of(systemMessage, userMessage),
			new GPTTriggerFormatResponseDTO());
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTTriggerRequestDTO requestDTO) {
		log.warn("Request to GPT Trigger Adapter");
		return webClient.post()
			.uri(GPTWebConfig.URI)
			.bodyValue(requestDTO)
			.exchange();
	}

	private GPTTriggerResponseDTO handleSuccessResponse(ClientResponse response) {
		GPTTriggerResponseDTO gptResponse = response.bodyToMono(GPTTriggerResponseDTO.class).block();
		log.warn("GPT response: {}", gptResponse);
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
