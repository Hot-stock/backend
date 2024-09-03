package com.bjcareer.payment.helper;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.payment.TossConfig;

import lombok.RequiredArgsConstructor;

@SpringBootConfiguration
@RequiredArgsConstructor
public class PSPWebClientConfiguration {
	public final TossConfig tossConfig;
	@Autowired
	private WebClient.Builder webClientBuilder;

	public WebClient tossPaymentWebClient(String errorCode){
		String encodedSecretKey = Base64.getEncoder().encodeToString((tossConfig.getSecretKey() + ":").getBytes());
		WebClient build = webClientBuilder.baseUrl(tossConfig.getUrl())
			.defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedSecretKey)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
			.defaultHeader("TossPayments-Test-Code", errorCode)
			.build();

		return build;
	}

}
