package com.bjcareer.search.out.api.naver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.domain.News;
import com.bjcareer.search.out.api.dto.NewsItemDTO;
import com.bjcareer.search.out.api.dto.NewsResponseDTO;
import com.bjcareer.search.out.api.python.ParseNewsContentResponseDTO;
import com.bjcareer.search.out.api.python.PythonSearchServerAdapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiNaverNews {
	private static final String API_URL = "https://openapi.naver.com/v1/search/news.json";
	private final String clientId;
	private final String clientSecret;
	private final RestTemplate restTemplate;
	private final PythonSearchServerAdapter pythonSearchServerAdapter;

	// 뉴스 검색 API 호출
	public List<News> fetchNews(NaverNewsQueryConfig config) {
		List<News> result = new ArrayList<>();

		NewsResponseDTO body = fetchNeverNewsAPI(config.buildUrl(API_URL));
		changeDtoToDomain(body, result);

		return result;
	}

	private NewsResponseDTO fetchNeverNewsAPI(String url) {
		HttpHeaders headers = createHeaders();
		NewsResponseDTO body = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
			NewsResponseDTO.class).getBody();
		return body;
	}

	private void changeDtoToDomain(NewsResponseDTO body, List<News> result) {
		for (NewsItemDTO itemDTO : body.getItems()) {
			ParseNewsContentResponseDTO newsBody = pythonSearchServerAdapter.getNewsBody(itemDTO.getLink());
			if (newsBody == null) {
				continue;
			}

			News news = new News(itemDTO.getTitle(), itemDTO.getOriginalLink(), itemDTO.getLink(),
				itemDTO.getDescription(),
				itemDTO.getPubDate(), newsBody.getData());

			result.add(news);
		}
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", clientId);
		headers.set("X-Naver-Client-Secret", clientSecret);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
