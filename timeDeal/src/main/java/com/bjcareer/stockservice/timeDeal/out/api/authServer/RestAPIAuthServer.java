package com.bjcareer.stockservice.timeDeal.out.api.authServer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.bjcareer.stockservice.timeDeal.application.ports.out.LoadUserPort;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RestAPIAuthServer implements LoadUserPort {
	private final RestClient client;

	public RestAPIAuthServer(@Qualifier("authWebClient") RestClient client) {
		this.client = client;
	}

	@Override
	public UserResponseDTO loadUserUsingSessionId(String sessionId) {
		log.info("Requesting user info from auth server with session id {}", sessionId);
		RestClient.ResponseSpec retrieve = client.get()
			.uri("/api/v0/users/" + sessionId)
			.retrieve();

		return retrieve.body(UserResponseDTO.class);
	}
}
