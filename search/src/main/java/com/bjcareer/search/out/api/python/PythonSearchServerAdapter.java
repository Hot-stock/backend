package com.bjcareer.search.out.api.python;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.domain.News;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PythonSearchServerAdapter {

	@Value("${python-search.address}")
	private String address;

	private final RestTemplate restTemplate;

	public void getNewsBody(News news) {
		log.debug("바디를 요청한 뉴스 링크는? : {}", news.getOriginalLink());
		String url = address + PythonServerURI.PARSE_CONTENT + news.getOriginalLink();

		ResponseEntity<ParseNewsContentResponseDTO> exchange = restTemplate.exchange(url, HttpMethod.GET, null,
			ParseNewsContentResponseDTO.class);

		HttpStatusCode statusCode = exchange.getStatusCode();

		if (!statusCode.is2xxSuccessful()) {
			log.error("Failed to get news body from python server. Status code: {}", statusCode);
			return;
		}

		ParseNewsContentResponseDTO body = exchange.getBody();
		news.setContent(body.getData());
	}
}
