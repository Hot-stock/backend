package com.bjcareer.search.out.api;

import java.security.SignatureException;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.out.api.dto.DataLabTrendRequestDTO;
import com.bjcareer.search.out.api.dto.KeywordResponseDTO;
import com.bjcareer.search.out.api.utils.Signatures;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiAdkeyword {
	private static final String API_URL = "https://api.searchad.naver.com";
	private static final String API_PATH = "/keywordstool";

	private final String CUSTOMER_ID;
	private final String ACCESS_KEY;
	private final String SECRET_KEY;
	private final RestTemplate restTemplate;

	public ApiAdkeyword(String customerId, String accessKey, String secretKey, RestTemplate restTemplate) {
		this.CUSTOMER_ID = customerId;
		this.ACCESS_KEY = accessKey;
		this.SECRET_KEY = secretKey;
		this.restTemplate = restTemplate;
	}

	public Optional<KeywordResponseDTO> getKeywordsCount(String keywords) {
		keywords = keywords.split(" ")[0];
		HttpHeaders header = createHeaders(API_PATH, Long.parseLong(CUSTOMER_ID), HttpMethod.GET);
		HttpEntity<DataLabTrendRequestDTO> entity = new HttpEntity<>(header);
		String path = API_URL + API_PATH + "?hintKeywords=" + keywords + "&showDetail=1";

		try {
			return Optional.of(restTemplate.exchange(path, HttpMethod.GET, entity, KeywordResponseDTO.class).getBody());
		} catch (HttpClientErrorException e) {
			log.error("Error fetching keywords", e);
			return Optional.empty();
		}
	}

	private HttpHeaders createHeaders(String path, long customerId, HttpMethod method) {
		HttpHeaders headers = new HttpHeaders();
		String timestamp = String.valueOf(System.currentTimeMillis());

		headers.set("X-Timestamp", timestamp);
		headers.set("X-API-KEY", ACCESS_KEY);
		headers.set("X-Customer", String.valueOf(customerId));

		try {
			headers.set("X-Signature", Signatures.of(timestamp, method.name(), path, SECRET_KEY));
		} catch (SignatureException e) {
			log.error("Error encoding signature", e);
		}

		return headers;
	}
}
