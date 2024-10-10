package com.bjcareer.search;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.out.api.naver.ApiAdkeyword;

@Configuration
public class NaverApiAdConfig {
	@Value("${naver.keyword-custom-id}")
	private String CUSTOMER_ID;
	@Value("${naver.keyword-access-key}")
	private String ACCESS_KEY;
	@Value("${naver.keyword-secret-key}")
	private String SECRET_KEY;

	@Bean
	public ApiAdkeyword apiAdkeyword(RestTemplate restTemplate) {
		return new ApiAdkeyword(CUSTOMER_ID, ACCESS_KEY, SECRET_KEY, restTemplate);
	}
}
