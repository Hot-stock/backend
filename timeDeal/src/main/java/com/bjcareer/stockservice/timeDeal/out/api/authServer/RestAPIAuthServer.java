package com.bjcareer.stockservice.timeDeal.out.api.authServer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.bjcareer.stockservice.timeDeal.application.ports.out.LoadUserPort;

@Configuration
public class RestAPIAuthServer implements LoadUserPort {
	private final RestClient client;

	public RestAPIAuthServer(@Qualifier("authWebClient") RestClient client) {
		this.client = client;
	}

	@Override
	public Long loadUserUsingSessionId(String sessionId) {
		RestClient.ResponseSpec retrieve = client.get()
			.uri("/api/v0/users/" + sessionId)
			.retrieve();

		return retrieve.body(Long.class);
	}
}
