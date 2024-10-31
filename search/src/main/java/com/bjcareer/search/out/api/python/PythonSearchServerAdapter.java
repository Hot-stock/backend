package com.bjcareer.search.out.api.python;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PythonSearchServerAdapter {

	@Value("${python-search.address}")
	private String address;

	private final RestTemplate restTemplate;

	public ParseNewsContentResponseDTO getNewsBody(String link) {
		log.debug("바디를 요청한 뉴스 링크는? : {}", link);
		String url = address + PythonServerURI.PARSE_CONTENT + link;

		ResponseEntity<ParseNewsContentResponseDTO> exchange = restTemplate.exchange(url, HttpMethod.GET, null,
			ParseNewsContentResponseDTO.class);

		HttpStatusCode statusCode = exchange.getStatusCode();

		if (!statusCode.is2xxSuccessful()) {
			log.error("Failed to get news body from python server. Status code: {}", statusCode);
			return null;
		}

		ParseNewsContentResponseDTO body = exchange.getBody();
		return body;
	}

	public List<OhlcResponseDTO> getStockOHLC(OHLCQueryConfig config) {
		String url = config.buildUrl(address + PythonServerURI.OHLC);

		ResponseEntity<List<OhlcResponseDTO>> exchange = restTemplate.exchange(url, HttpMethod.GET, null,
			new ParameterizedTypeReference<List<OhlcResponseDTO>>() {});

		HttpStatusCode statusCode = exchange.getStatusCode();

		if (!statusCode.is2xxSuccessful()) {
			log.error("Failed to get news body from python server. Status code: {}", statusCode);
			return null;
		}

		List<OhlcResponseDTO> body = exchange.getBody();
		return body;

	}
}
