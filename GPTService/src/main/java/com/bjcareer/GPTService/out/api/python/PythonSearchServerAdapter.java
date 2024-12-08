package com.bjcareer.GPTService.out.api.python;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PythonSearchServerAdapter {

	@Value("${python-search.address}")
	private String address;
	private final WebClient webClient;

	public List<NewsResponseDTO> fetchNews(NewsCommand command) {
		String url = command.buildUrl(address + PythonServerURI.NEWS);
		log.debug("Requesting news for url: {}", url);

		Optional<List<NewsResponseDTO>> newsResponseDTOS = fetchFromServer(url,
			new ParameterizedTypeReference<List<NewsResponseDTO>>() {
			});

		if (newsResponseDTOS.isEmpty()) {
			log.debug("No news found for command: {}", command);
			return List.of();
		}

		return newsResponseDTOS.get();
	}

	public Optional<OriginalNews> fetchNewsBody(String link, LocalDate date) {
		String encodedLink = URLEncoder.encode(link, StandardCharsets.UTF_8);
		String url = address + PythonServerURI.PARSE_CONTENT + encodedLink;
		log.debug("Requesting news body for link: {}", url);

		Optional<ParseNewsContentResponseDTO> responseDTO = fetchFromServer(url,
			new ParameterizedTypeReference<>() {
			});

		if (responseDTO.isEmpty()) {
			log.error("Failed to fetch news body for link: {}", link);
			return Optional.empty();
		} else {
			log.debug("Fetched news body for link: {}", link);
			ParseNewsContentResponseDTO parseNewsContentResponseDTO = responseDTO.get();

			if (parseNewsContentResponseDTO.getPublishDate() == null){
				parseNewsContentResponseDTO.setPublishDate(date.toString());
			}
			return Optional.of(new OriginalNews(parseNewsContentResponseDTO.getTitle(), link,
				parseNewsContentResponseDTO.getImgLink(), date.toString(),
				parseNewsContentResponseDTO.getText()));
		}
	}

	// 공통 REST 호출 메서드로 재사용성 향상
	private <T> Optional<T> fetchFromServer(String url, ParameterizedTypeReference<T> responseType) {
		try {
			ClientResponse response = webClient.get()
				.uri(url)
				.exchange()
				.timeout(Duration.ofSeconds(30))
				.block();

			if (response.statusCode().is2xxSuccessful()) {
				return Optional.ofNullable(response.bodyToMono(responseType).block());
			} else {
				log.error("Failed to fetch from server. URL: {}, Status Code: {}", url, response.statusCode());
				return Optional.empty();
			}
		} catch (RuntimeException e) {
			log.error("Exception while fetching data from server. URL: {}, Error: {}", url, e.getMessage());
			return Optional.empty();
		}
	}
}
