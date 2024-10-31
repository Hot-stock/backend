package com.bjcareer.search.config.naver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.out.api.naver.ApiNaverNews;
import com.bjcareer.search.out.api.python.PythonSearchServerAdapter;

@Configuration
public class NaverApiNewsConfig {

	@Value("${naver.client-new-id}")
	private String CLIENT_ID;
	@Value("${naver.client-new-secret}")
	private String SECRET_KEY;

	@Bean
	public ApiNaverNews apiNaverNews(RestTemplate restTemplate, PythonSearchServerAdapter pythonSearchServerAdapter) {
		return new ApiNaverNews(CLIENT_ID, SECRET_KEY, restTemplate, pythonSearchServerAdapter);
	}
}
