package com.bjcareer.gateway.out.api.auth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.gateway.application.ports.in.LoginCommand;
import com.bjcareer.gateway.application.ports.in.LogoutCommand;
import com.bjcareer.gateway.application.ports.in.TokenRefreshCommand;
import com.bjcareer.gateway.application.ports.out.AuthServerPort;
import com.bjcareer.gateway.common.CookieHelper;
import com.bjcareer.gateway.domain.JWTDomain;
import com.bjcareer.gateway.exceptions.UnauthorizedAccessAttemptException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServerAPIAdapter implements AuthServerPort {
	private final WebClient webClient;

	public AuthServerAPIAdapter(@Qualifier("authWebClient") WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public JWTDomain login(LoginCommand loginCommand) {
		ClientResponse response = getClientResponse(AuthServerURI.LOGIN, loginCommand);
		log.info("Response status of Auth Server for login Request: {}", response.statusCode());

		if (response.statusCode().is2xxSuccessful()) {
			String accessToken = getCookie(response, CookieHelper.ACCESS_TOKEN);
			String refreshToken = getCookie(response, CookieHelper.REFRESH_TOKEN);
			String sessionId = getCookie(response, CookieHelper.SESSION_ID);

			log.debug("Cookies {} {} {}", accessToken, refreshToken, sessionId);
			return new JWTDomain(accessToken, refreshToken, sessionId);
		}

		throw new UnauthorizedAccessAttemptException("로그인에 실패했습니다.");
	}

	@Override
	public boolean logout(LogoutCommand logoutCommand) {
		ClientResponse clientResponse = webClient.post()
			.uri(AuthServerURI.LOGOUT)
			.cookies(c -> c.add(CookieHelper.ACCESS_TOKEN, logoutCommand.getAccessToken()))
			.cookies(c -> c.add(CookieHelper.SESSION_ID, logoutCommand.getSessionId()))
			.exchange().block();

		log.info("Response status of Auth Server for logout Request: {}", clientResponse.statusCode());
		if (clientResponse.statusCode().is2xxSuccessful()) {
			return true;
		}

		return false;
	}

	@Override
	public JWTDomain refresh(TokenRefreshCommand command) {
		ClientResponse response = getClientResponse(AuthServerURI.REFRESH, command);
		log.info("Response status of Auth Server for refresh Request: {}", response.statusCode());

		if (response.statusCode().is2xxSuccessful()) {
			String accessToken = getCookie(response, CookieHelper.ACCESS_TOKEN);
			String refreshToken = getCookie(response, CookieHelper.REFRESH_TOKEN);
			String sessionId = getCookie(response, CookieHelper.SESSION_ID);

			log.debug("Cookies {} {} {}", accessToken, refreshToken, sessionId);
			return new JWTDomain(accessToken, refreshToken, sessionId);
		}

		throw new UnauthorizedAccessAttemptException("로그인에 실패했습니다.");
	}

	private ClientResponse getClientResponse(String uri, Object command) {
		return webClient.post()
			.uri(uri)
			.bodyValue(command)
			.exchange()
			.block();
	}

	private String getCookie(ClientResponse response, String cookieName) {
		return response.cookies().get(cookieName).get(0).getValue();
	}
}
