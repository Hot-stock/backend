package com.bjcareer.userservice.application.auth.token.valueObject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenValidationResult {
	private final boolean valid;
	private final boolean expired;
	private final String message;
}

