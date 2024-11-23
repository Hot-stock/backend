package com.bjcareer.GPTService.out.api.python;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
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
		List<OriginalNews> newsList = new ArrayList<>();
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

	public ParseNewsContentResponseDTO fetchNewsBody(String link) {
		String encodedLink = URLEncoder.encode(link, StandardCharsets.UTF_8);
		String url = address + PythonServerURI.PARSE_CONTENT + encodedLink;
		log.debug("Requesting news body for link: {}", url);

		Optional<ParseNewsContentResponseDTO> responseDTO = fetchFromServer(url,
			new ParameterizedTypeReference<>() {
			});
		ParseNewsContentResponseDTO parseNewsContentResponseDTO = responseDTO.orElseGet(
			ParseNewsContentResponseDTO::new);

		return parseNewsContentResponseDTO;
	}

	// 공통 REST 호출 메서드로 재사용성 향상
	private <T> Optional<T> fetchFromServer(String url, ParameterizedTypeReference<T> responseType) {
		try {
			ClientResponse response = webClient.get()
				.uri(url)
				.exchange().block();

			if (response.statusCode().is2xxSuccessful()) {
				return Optional.ofNullable(response.bodyToMono(responseType).block());
			} else {
				log.error("Failed to fetch from server. URL: {}, Status Code: {}", url, response.statusCode());
			}
		} catch (RestClientException e) {
			log.error("Exception while fetching data from server. URL: {}, Error: {}", url, e.getMessage());
		}
		return Optional.empty();
	}
}
