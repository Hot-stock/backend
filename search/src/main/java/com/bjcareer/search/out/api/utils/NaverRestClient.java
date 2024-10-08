package com.bjcareer.search.out.api.utils;

import java.security.SignatureException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class NaverRestClient {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final boolean DEBUG_MODE = true;

	private final RestTemplate restTemplate;
	private final String baseUrl;
	private final String apiKey;
	private final String secretKey;

	public NaverRestClient(RestTemplate restTemplate, String baseUrl, String apiKey, String secretKey) {
		this.restTemplate = restTemplate;
		this.baseUrl = baseUrl;
		this.apiKey = apiKey;
		this.secretKey = secretKey;
	}

	// GET 요청 메소드
	public ResponseEntity<String> get(String path, long customerId) throws SignatureException {
		HttpHeaders headers = createHeaders(path, customerId, HttpMethod.GET);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		return restTemplate.exchange("https://api.searchad.naver.com/keywordstool?hintKeywords=강아지&showDetail=1",
			HttpMethod.GET, entity, String.class);
	}

	// POST 요청 메소드
	public ResponseEntity<String> post(String path, long customerId, Object body) throws SignatureException {
		HttpHeaders headers = createHeaders(path, customerId, HttpMethod.POST);
		headers.set("Content-Type", "application/json;charset=UTF-8");
		HttpEntity<String> entity = new HttpEntity<>(toJson(body), headers);
		return restTemplate.exchange(baseUrl + path, HttpMethod.POST, entity, String.class);
	}

	// PUT 요청 메소드
	public ResponseEntity<String> put(String path, long customerId, Object body) throws SignatureException {
		HttpHeaders headers = createHeaders(path, customerId, HttpMethod.PUT);
		headers.set("Content-Type", "application/json;charset=UTF-8");
		HttpEntity<String> entity = new HttpEntity<>(toJson(body), headers);
		return restTemplate.exchange(baseUrl + path, HttpMethod.PUT, entity, String.class);
	}

	// DELETE 요청 메소드
	public ResponseEntity<String> delete(String path, long customerId) throws SignatureException {
		HttpHeaders headers = createHeaders(path, customerId, HttpMethod.DELETE);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		return restTemplate.exchange(baseUrl + path, HttpMethod.DELETE, entity, String.class);
	}

	// 요청 헤더 생성
	private HttpHeaders createHeaders(String path, long customerId, HttpMethod method) throws SignatureException {
		HttpHeaders headers = new HttpHeaders();
		String timestamp = String.valueOf(System.currentTimeMillis());

		headers.set("X-Timestamp", timestamp);
		headers.set("X-API-KEY", apiKey);
		headers.set("X-Customer", String.valueOf(customerId));
		headers.set("X-Signature", Signatures.of(timestamp, method.name(), path, secretKey));

		if (DEBUG_MODE) {
			System.out.println("Request Headers: " + headers);
		}

		return headers;
	}

	// 객체를 JSON 문자열로 변환
	private String toJson(Object object) {
		try {
			String json = OBJECT_MAPPER.writeValueAsString(object);
			if (DEBUG_MODE) {
				System.out.println("Request Body: " + json);
			}
			return json;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	// 응답 처리
	public <T> T asObject(ResponseEntity<String> response, Class<T> type) throws Exception {
		String responseBody = response.getBody();
		if (DEBUG_MODE && responseBody != null) {
			System.out.println("Response Body: " + responseBody);
		}

		int status = response.getStatusCodeValue();
		if (status / 100 != 2) {
			throw new Exception("Error: " + response.getStatusCode() + ", Response Body: " + responseBody);
		}
		return OBJECT_MAPPER.readValue(responseBody, type);
	}
}
