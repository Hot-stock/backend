package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.themaName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
	public static final String MODEL = "gpt-4o";
	private final WebClient webClient;
	private final RedisThemaRepository redisThemaRepository;

	public Optional<ThemaInfo> getThemaName(List<String> stockNames, String themaName, String reason) {
		UUID uuid = UUID.randomUUID();

		redisThemaRepository.getLock();
		List<String> themas = redisThemaRepository.loadThema();

		log.debug("{} Load Themas: {}", uuid, themas);
		GPTThemaNameRequestDTO requestDTO = createRequestDTO(themaName, reason,
			themas.stream().collect(Collectors.joining(", ")));
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
			log.debug("{} extract Themas: {}, contain {}", uuid, parsedContent.getThema(), parsedContent.getThemasName());
			ThemaInfo themaInfo = new ThemaInfo(stockNames, parsedContent.getThema(), parsedContent.getReason());
			themaInfo.changeThemaNameUsingLevenshteinDistance(themas);
			redisThemaRepository.updateThema(themaInfo.getName());
			log.debug("{} last ThemaInfo: {}", uuid, themaInfo.getName());
			redisThemaRepository.releaseLock();

			return Optional.of(themaInfo);
		} else {
			handleErrorResponse(response);
			return Optional.empty();
		}
	}

	private GPTThemaNameRequestDTO createRequestDTO(String themaName, String reason, String themas) {
		GPTThemaNameRequestDTO.Message systemMessage = new GPTThemaNameRequestDTO.Message(
			GPTWebConfig.SYSTEM_ROLE, GPTWebConfig.SYSTEM_MESSAGE_TEXT + GPTWebConfig.SYSTEM_THEMA_TEXT);
		GPTThemaNameRequestDTO.Message userMessage = new GPTThemaNameRequestDTO.Message(GPTWebConfig.USER_ROLE,
			AnalyzeThemaNamePrompt.PROMPT.formatted(themaName, reason, themas));

		GPTResponseThemaNameOfThemaFormatDTO gptResponseThemaFormatDTO = new GPTResponseThemaNameOfThemaFormatDTO();

		return new GPTThemaNameRequestDTO(MODEL, List.of(systemMessage, userMessage), gptResponseThemaFormatDTO);
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
