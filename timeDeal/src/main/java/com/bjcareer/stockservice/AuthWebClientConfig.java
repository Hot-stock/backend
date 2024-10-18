package com.bjcareer.stockservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AuthWebClientConfig {
	@Value("${auth.url}")
	private String baseURL;

	@Bean("authWebClient")
	public RestClient webClient(RestClient.Builder builder) {
		builder.baseUrl(baseURL)
			.defaultHeader("Content-Type", "application/json");
		return builder.build();
	}
}
