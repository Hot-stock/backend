package com.bjcareer.search;

import org.springframework.web.reactive.function.client.WebClient;

public class GPTWebTestConfig {
	private final String API_KEY = "";
	private String baseURL = "https://api.openai.com/v1/";

	public WebClient webClient() {
		WebClient.Builder builder = WebClient.builder();
		builder.baseUrl(baseURL)
			.defaultHeader("Content-Type", "application/json")
			.defaultHeader("Authorization", "Bearer " + API_KEY);
		return builder.build();
	}
}
