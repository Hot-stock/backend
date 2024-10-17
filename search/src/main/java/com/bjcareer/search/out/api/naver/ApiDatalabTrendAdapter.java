package com.bjcareer.search.out.api.naver;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.application.port.out.NaverDataTrendPort;
import com.bjcareer.search.out.api.dto.DataLabTrendRequestDTO;
import com.bjcareer.search.out.api.dto.DataLabTrendResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiDatalabTrendAdapter implements NaverDataTrendPort {
	private static final String API_URL = "https://openapi.naver.com/v1/datalab/search";
	private final String clientId;
	private final String clientSecret;

	private final RestTemplate restTemplate;

	public ApiDatalabTrendAdapter(String clientId, String clientSecret, RestTemplate restTemplate) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.restTemplate = restTemplate;
	}

	public Optional<DataLabTrendResponseDTO> fetchTrends(DataLabTrendRequestDTO request) {
		HttpHeaders headers = createHeaders();
		HttpEntity<DataLabTrendRequestDTO> entity = new HttpEntity<>(request, headers);

		try {
			DataLabTrendResponseDTO body = restTemplate.exchange(
				API_URL,
				HttpMethod.POST,
				entity,
				DataLabTrendResponseDTO.class).getBody();

			if (body == null) {
				log.error("Received null response from API for request: {}", request);
				return Optional.empty();
			}

			insertZeroPointToEmptyData(request, body);
			return Optional.of(body);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			log.error("Client/Server error while fetching trends for request: {}", request, e);
		} catch (RestClientException e) {
			log.error("Error fetching trends for request: {}", request, e);
		}
		return Optional.empty();
	}

	private void insertZeroPointToEmptyData(DataLabTrendRequestDTO request, DataLabTrendResponseDTO body) {
		body.getResults().forEach(result -> {
			if (result.getData().isEmpty()) {
				result.getData().add(new DataLabTrendResponseDTO.Result.Info(request.getStartDate(), 0.0));
			}
		});
	}

	// 클라이언트 ID와 시크릿을 사용해 HttpHeaders 생성
	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", clientId);
		headers.set("X-Naver-Client-Secret", clientSecret);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
