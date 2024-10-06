package com.bjcareer.gateway.application.ports.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenRefreshCommand {
	private final String sessionId;
	private final String refreshToken;
}
