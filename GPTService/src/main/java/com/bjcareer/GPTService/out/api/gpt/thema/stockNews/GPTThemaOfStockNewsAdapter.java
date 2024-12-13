package com.bjcareer.GPTService.out.api.gpt.thema.stockNews;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.GPTService.config.gpt.GPTWebConfig;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;
import com.bjcareer.GPTService.domain.gpt.thema.ThemaInfo;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTThemaOfStockNewsAdapter {
	public static final String MODEL = "ft:gpt-4o-2024-08-06:personal::AeDV6Sq6";
	private final WebClient webClient;
	private final RedisThemaRepository redisThemaRepository;

	public Optional<GPTStockThema> getThema(GPTNewsDomain stockNews, String knownThema) {
		GPTThemaOfStockRequestDTO requestDTO = createRequestDTO(stockNews.getNews().getContent(),
			stockNews.getNews().getPubDate(), knownThema, stockNews.getStockName());
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTThemaOfStockNewsResponseDTO gptThemaResponseDTO = handleSuccessResponse(response);
			GPTThemaOfStockNewsResponseDTO.Content parsedContent = gptThemaResponseDTO.getChoices()
				.get(0)
				.getMessage()
				.getParsedContent();

			List<String> themas = redisThemaRepository.loadThema();

			if(parsedContent == null) {
				log.warn("Parsed content is null");
				return Optional.empty();
			}

			List<ThemaInfo> themaInfos = parsedContent.getThema()
				.stream()
				.map(t -> new ThemaInfo(t.getStockNames(), t.getName(), t.getReason()))
				.toList();

			themaInfos.stream().filter(t -> t.getStockName().contains(stockNews.getStockName()))
				.forEach(t -> {
					t.changeThemaNameUsingLevenshteinDistance(themas);
					themas.add(t.getName());
				});

			return Optional.of(
				new GPTStockThema(stockNews.getLink(), parsedContent.isPositive(), themaInfos));
		} else {
			handleErrorResponse(response);
			return Optional.empty();
		}
	}

	private GPTThemaOfStockRequestDTO createRequestDTO(String content, LocalDate pubDate, String knownThema,
		String stockName) {
		GPTThemaOfStockRequestDTO.Message systemMessage = new GPTThemaOfStockRequestDTO.Message(
			GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + GPTWebConfig.SYSTEM_THEMA_TEXT);

		GPTThemaOfStockRequestDTO.Message userMessage = new GPTThemaOfStockRequestDTO.Message(
			GPTWebConfig.USER_ROLE,
			AnalyzeThemaQuestionPrompt.QUESTION_PROMPT.formatted(pubDate, content, stockName, knownThema));

		GPTResponseStockNewsOfThemaFormatDTO gptResponseThemaFormatDTO = new GPTResponseStockNewsOfThemaFormatDTO();

		return new GPTThemaOfStockRequestDTO(MODEL, List.of(systemMessage, userMessage), gptResponseThemaFormatDTO);
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTThemaOfStockRequestDTO requestDTO) {
		return webClient.post().uri(GPTWebConfig.URI).bodyValue(requestDTO).exchange();
	}

	private GPTThemaOfStockNewsResponseDTO handleSuccessResponse(ClientResponse response) {
		// 동기적으로 body를 읽음
		GPTThemaOfStockNewsResponseDTO gptResponse = response.bodyToMono(GPTThemaOfStockNewsResponseDTO.class).block();
		log.debug("Response body: {}", gptResponse);
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
