package com.bjcareer.payment.adapter.out.web.config;

import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.payment.TossConfig;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TossWebClientConfiguration {
	private final TossConfig tossConfig;
	@Bean
	public WebClient tossPaymentWebClient(WebClient.Builder webClientBuilder){
		String encodedSecretKey = Base64.getEncoder().encodeToString((tossConfig.getSecretKey() + ":").getBytes());
		WebClient build = webClientBuilder.baseUrl(tossConfig.getUrl())
			.defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedSecretKey)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json").build();

		System.out.println("tossConfig.getUrl() = " + tossConfig.getUrl());
		return build;
	}
}
