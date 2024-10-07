package com.bjcareer.gateway.out.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.gateway.application.ports.out.LoginCommandPort;
import com.bjcareer.gateway.domain.JWTDomain;

class AuthServerAPIAdapterTest {
	@Test
	void login() {
		// Given
		LoginCommandPort loginCommand = new LoginCommandPort("wodhksqw@naver.com", "friend77asd@");
		WebClient webClient = createWebClient("http://3.34.191.223:8080");
		AuthServerAPIAdapter authServerAPIAdapter = new AuthServerAPIAdapter(webClient);
		JWTDomain res = authServerAPIAdapter.login(loginCommand);

		assertNotNull(res.getAccessToken());
		assertNotNull(res.getRefreshToken());
		assertNotNull(res.getSessionId());
	}

	WebClient createWebClient(String baseUrl) {
		return WebClient.builder()
			.baseUrl(baseUrl)
			.defaultHeader("Content-Type", "application/json")
			.build();
	}

}
