package com.bjcareer.search.out.api;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.out.api.dto.DataLabTrendRequestDTO;
import com.bjcareer.search.out.api.dto.DataLabTrendResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiExamDatalabTrend {
	private static final String API_URL = "https://openapi.naver.com/v1/datalab/search";
	private static final String CLIENT_ID = "oxSx5BNIEkBLgUKERT5c";
	private static final String CLIENT_SECRET = "MEyDrs38fL";
	private final RestTemplate restTemplate;

	public Optional<DataLabTrendResponseDTO> fetchTrends(DataLabTrendRequestDTO request) {
		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", CLIENT_ID);
		headers.set("X-Naver-Client-Secret", CLIENT_SECRET);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<DataLabTrendRequestDTO> entity = new HttpEntity<>(request, headers);

		try {
			return Optional.ofNullable(restTemplate.exchange(
				API_URL,
				HttpMethod.POST,
				entity,
				DataLabTrendResponseDTO.class
			).getBody());
		} catch (RestClientException e) {
			log.error("Failed to fetch trends from Naver Datalab", e);
			return Optional.empty();
		}
	}
}
