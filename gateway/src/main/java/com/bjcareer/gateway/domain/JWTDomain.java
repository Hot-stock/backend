package com.bjcareer.gateway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JWTDomain {
	private final String accessToken;
	private final String refreshToken;
	private final String sessionId;
}
