package com.bjcareer.search;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.out.api.ApiDatalabTrend;

public class NaverApiTrendConfig {

	@Value("${naver.trend-client-id}")
	private String CLIENT_ID;
	@Value("${naver.trend-client-secret}")
	private String SECRET_KEY;

	@Bean
	public ApiDatalabTrend apiExamDatalabTrend(RestTemplate restTemplate) {
		return new ApiDatalabTrend(CLIENT_ID, SECRET_KEY, restTemplate);
	}
}
