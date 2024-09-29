package com.bjcareer.userservice.application.auth.ports;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenRefreshCommand {
	private final String sessionId;
	private final String refreshToken;

	public String getSessionId() {
		return sessionId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}
