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
import com.bjcareer.GPTService.out.api.gpt.thema.ThemaVariableResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.dtos.GPTResponseStockNewsOfThemaFormatDTO;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.dtos.GPTThemaOfStockNewsResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.dtos.GPTThemaOfStockRequestDTO;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.prompt.AnalyzeThemaQuestionPrompt;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTThemaOfStockNewsAdapter {
	private final WebClient webClient;
	private final RedisThemaRepository redisThemaRepository;
	private final String GPT_4o_MINI = "gpt-4o-mini";
	private final String GPT_4o = "gpt-4o";



	public Optional<GPTStockThema> getThema(GPTNewsDomain stockNews) {
		redisThemaRepository.getLock();

		List<String> themas = redisThemaRepository.loadThema();
		String themasStr = String.join(",", themas);

		GPTThemaOfStockRequestDTO requestDTO = createRequestDTO(stockNews.getNews().getContent(),
			stockNews.getNews().getPubDate(), stockNews.getStockName(), themasStr, GPT_4o_MINI);
		ClientResponse response = sendRequestToGPT(requestDTO).block();

		if (response != null && response.statusCode().is2xxSuccessful()) {
			GPTThemaOfStockNewsResponseDTO gptThemaResponseDTO = handleSuccessResponse(response);
			GPTThemaOfStockNewsResponseDTO.Content parsedContent = gptThemaResponseDTO.getChoices()
				.get(0)
				.getMessage()
				.getParsedContent();

			if(parsedContent == null) {
				log.warn("Parsed content is null");
				return Optional.empty();
			}

			ThemaVariableResponseDTO thema = parsedContent.getThema();

			if (!themasStr.contains(thema.getName())) {
				requestDTO = createRequestDTO(stockNews.getNews().getContent(),
					stockNews.getNews().getPubDate(), stockNews.getStockName(), themasStr, GPT_4o);
				response = sendRequestToGPT(requestDTO).block();
				GPTThemaOfStockNewsResponseDTO gptThemaOfStockNewsResponseDTO = handleSuccessResponse(response);
				parsedContent = gptThemaOfStockNewsResponseDTO.getChoices()
					.get(0)
					.getMessage()
					.getParsedContent();

				if (parsedContent == null) {
					log.warn("Parsed content is null");
					return Optional.empty();
				}

				thema = parsedContent.getThema();
			}

			ThemaInfo themaInfo = new ThemaInfo(thema.getStockNames(), thema.getName(), thema.getReason());
			GPTStockThema gptStockThema = new GPTStockThema(stockNews.getLink(), parsedContent.isPositive(),
				themaInfo);

			redisThemaRepository.updateThema(themaInfo.getName());
			redisThemaRepository.releaseLock();

			return Optional.of(gptStockThema);
		} else {
			handleErrorResponse(response);
			return Optional.empty();
		}
	}

	private GPTThemaOfStockRequestDTO createRequestDTO(String content, LocalDate pubDate, String stockName,
		String themas, String model) {
		GPTThemaOfStockRequestDTO.Message systemMessage = new GPTThemaOfStockRequestDTO.Message(
			GPTWebConfig.SYSTEM_ROLE,
			GPTWebConfig.SYSTEM_MESSAGE_TEXT + GPTWebConfig.SYSTEM_THEMA_TEXT);

		GPTThemaOfStockRequestDTO.Message userMessage = new GPTThemaOfStockRequestDTO.Message(
			GPTWebConfig.USER_ROLE,
			AnalyzeThemaQuestionPrompt.QUESTION_PROMPT.formatted(pubDate, content, stockName, themas));

		GPTResponseStockNewsOfThemaFormatDTO gptResponseThemaFormatDTO = new GPTResponseStockNewsOfThemaFormatDTO();

		return new GPTThemaOfStockRequestDTO(model, List.of(systemMessage, userMessage), gptResponseThemaFormatDTO);
	}

	private Mono<ClientResponse> sendRequestToGPT(GPTThemaOfStockRequestDTO requestDTO) {
		return webClient.post().uri(GPTWebConfig.URI).bodyValue(requestDTO).exchange();
	}

	private GPTThemaOfStockNewsResponseDTO handleSuccessResponse(ClientResponse response) {
		// 동기적으로 body를 읽음
		GPTThemaOfStockNewsResponseDTO gptResponse = response.bodyToMono(GPTThemaOfStockNewsResponseDTO.class).block();
		log.debug("Response: {}", gptResponse);
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
