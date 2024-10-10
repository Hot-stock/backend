package com.bjcareer.search.out.api.naver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.domain.NewsDomain;
import com.bjcareer.search.out.api.dto.NewsResponseDTO;

public class ApiNaverNews {
	private static final String API_URL = "https://openapi.naver.com/v1/search/news.json";
	private final String clientId;
	private final String clientSecret;
	private final RestTemplate restTemplate;

	public ApiNaverNews(String clientId, String clientSecret, RestTemplate restTemplate) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.restTemplate = restTemplate;
	}

	// 뉴스 검색 API 호출
	public List<NewsDomain> fetchNews(String keyword) {
		List<NewsDomain> result = new ArrayList<>();
		HttpHeaders headers = createHeaders();
		String url = API_URL + "?display=30&start=1&sort=sim&query=" + keyword;
		NewsResponseDTO body = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
			NewsResponseDTO.class).getBody();
		body.getItems().forEach(item ->
			result.add(new NewsDomain(item.getTitle(), item.getOriginalLink(), item.getLink(), item.getDescription(),
				item.getPubDate())));
		return result;
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
