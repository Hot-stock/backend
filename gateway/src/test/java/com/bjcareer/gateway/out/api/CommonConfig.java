package com.bjcareer.gateway.out.api;

import org.springframework.web.reactive.function.client.WebClient;

public class CommonConfig {

	public static WebClient createWebClient(String baseUrl) {
		return WebClient.builder()
			.baseUrl(baseUrl)
			.defaultHeader("Content-Type", "application/json")
			.build();
	}
}
