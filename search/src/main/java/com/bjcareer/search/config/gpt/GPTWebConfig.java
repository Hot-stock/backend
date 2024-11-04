package com.bjcareer.search.config.gpt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GPTWebConfig {
	@Value("${GPT.api-key}")
	private String API_KEY;
	private String baseURL = "https://api.openai.com/v1/";

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		builder.baseUrl(baseURL)
			.defaultHeader("Content-Type", "application/json")
			.defaultHeader("Authorization", "Bearer " + API_KEY);
		return builder.build();
	}
}
