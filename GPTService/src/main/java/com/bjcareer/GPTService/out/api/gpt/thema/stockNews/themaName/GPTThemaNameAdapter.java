package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.themaName;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.GPTService.config.gpt.GPTWebConfig;
import com.bjcareer.GPTService.domain.gpt.thema.ThemaInfo;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTThemaNameAdapter {
	public static final String gpt_4o = "gpt-4o";
	public static final String gpt_4o_mini = "gpt-4o-mini";
	private final WebClient webClient;
	private final RedisThemaRepository redisThemaRepository;

	public Optional<ThemaInfo> getThemaName(List<String> stockNames, String themaName, String reason) {

		redisThemaRepository.getLock();
		List<String> themas = redisThemaRepository.loadThema();

		GPTThemaNameRequestDTO requestDTO = createRequestDTO(themaName, reason,
			themas.stream().collect(Collectors.joining(", ")), gpt_4o_mini);
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTThemaNameResponseDTO gptThemaResponseDTO = handleSuccessResponse(response);
			GPTThemaNameResponseDTO.Content parsedContent = gptThemaResponseDTO.getChoices()
				.get(0)
				.getMessage()
				.getParsedContent();

			if (parsedContent == null) {
				log.warn("Parsed content is null");
				return Optional.empty();
			}
			ThemaInfo themaInfo = new ThemaInfo(stockNames, parsedContent.getThema(), reason);
			themaInfo.changeThemaNameUsingLevenshteinDistance(themas);

			if (!themas.contains(themaInfo.getName())) {
				themas.remove(themaInfo.getName());

				requestDTO = createRequestDTO(themaName, reason,
					themas.stream().collect(Collectors.joining(", ")), gpt_4o);
				response = sendRequestToGPT(requestDTO).block();

				gptThemaResponseDTO = handleSuccessResponse(response);
				parsedContent = gptThemaResponseDTO.getChoices()
					.get(0)
					.getMessage()
					.getParsedContent();

				if (parsedContent == null) {
					log.warn("Parsed content is null");
					return Optional.empty();
				}
				log.debug("thema: {} reason {}", parsedContent.getThemasName(), parsedContent.getReason());

				themaInfo = new ThemaInfo(stockNames, parsedContent.getThema(), reason);
				themaInfo.changeThemaNameUsingLevenshteinDistance(themas);
			}

			redisThemaRepository.updateThema(themaInfo.getName());
			redisThemaRepository.releaseLock();

			return Optional.of(themaInfo);
		} else {
			handleErrorResponse(response);
			redisThemaRepository.releaseLock();
			return Optional.empty();
		}
	}

	private GPTThemaNameRequestDTO createRequestDTO(String themaName, String reason, String themas, String model) {
		log.debug("Request to GPT with Reason: {}, model: {}", reason, model);
		GPTThemaNameRequestDTO.Message systemMessage = new GPTThemaNameRequestDTO.Message(
			GPTWebConfig.SYSTEM_ROLE, GPTWebConfig.SYSTEM_MESSAGE_TEXT + GPTWebConfig.SYSTEM_THEMA_TEXT);
		GPTThemaNameRequestDTO.Message userMessage = new GPTThemaNameRequestDTO.Message(GPTWebConfig.USER_ROLE,
			AnalyzeThemaNamePrompt.PROMPT.formatted(themaName, reason, themas));

		GPTResponseThemaNameOfThemaFormatDTO gptResponseThemaFormatDTO = new GPTResponseThemaNameOfThemaFormatDTO();

		return new GPTThemaNameRequestDTO(model, List.of(systemMessage, userMessage), gptResponseThemaFormatDTO);
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTThemaNameRequestDTO requestDTO) {
		return webClient.post().uri(GPTWebConfig.URI).bodyValue(requestDTO).exchange();
	}

	private GPTThemaNameResponseDTO handleSuccessResponse(ClientResponse response) {
		// 동기적으로 body를 읽음
		GPTThemaNameResponseDTO gptResponse = response.bodyToMono(GPTThemaNameResponseDTO.class).block();
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
