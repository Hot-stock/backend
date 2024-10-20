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

		// RestClient를 통해 GET 요청 보내기
		RestClient.ResponseSpec retrieve = client.get()
			.uri("/api/v0/users/" + sessionId)
			.retrieve()
			.onStatus(
				status -> !status.is2xxSuccessful(),
				(request, response) -> {
					// 에러 발생 시 로그 출력 및 예외 발생
					log.error("요청 URI {} status code {}", request.getURI(), response.getStatusCode());
					log.error("Failed to retrieve user info from auth server with session id {}", sessionId);
				}
			);

		// 2xx 상태일 경우 응답 본문을 UserResponseDTO로 변환하여 반환
		return retrieve.body(UserResponseDTO.class);
	}

}
