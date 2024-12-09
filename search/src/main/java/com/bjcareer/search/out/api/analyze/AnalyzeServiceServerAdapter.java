package com.bjcareer.search.out.api.analyze;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.out.api.python.ParseNewsContentResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AnalyzeServiceServerAdapter {
	@Value("${analyze-search.address}")
	private String address;
	private static final String URI = "/api/v0/analyze/stock-news";
	private final RestTemplate restTemplate;

	public void updateNewsOfStock(String stockName, String date) {
		log.debug("Requesting news body for link: {}", URI);

		// 요청 본문 구성
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("stockName", stockName);
		requestBody.put("date", date);

		// HttpEntity에 요청 본문을 설정
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody);

		// API 호출
		ResponseEntity<String> response = restTemplate.exchange(
			address + URI,
			HttpMethod.POST,
			requestEntity,
			String.class
		);

		// 응답 처리
		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			log.debug("Response received: {}", response.getBody());
		} else {
			log.error("Failed to fetch news. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
		}
	}
}
