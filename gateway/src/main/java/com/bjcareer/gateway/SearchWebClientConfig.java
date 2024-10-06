package com.bjcareer.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SearchWebClientConfig {
	@Value("${search.url}")
	private String baseURL;

	@Bean("searchWebClient")
	public WebClient webClient(WebClient.Builder builder) {
		builder.baseUrl(baseURL)
			.defaultHeader("Content-Type", "application/json");
		return builder.build();
	}
}
