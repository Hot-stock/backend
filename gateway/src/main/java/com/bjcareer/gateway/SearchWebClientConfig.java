package com.bjcareer.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class SearchWebClientConfig {
	@Value("${search.url}")
	private String baseURL;

	@Bean("searchWebClient")
	public WebClient webClient(WebClient.Builder builder) {
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseURL);
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

		builder.baseUrl(baseURL)
			.uriBuilderFactory(factory)
			.defaultHeader("Content-Type", "application/json");

		return builder.build();
	}
}
