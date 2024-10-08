package com.bjcareer.gateway.application.ports.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class VerifyTokenCommand {
	private final String email;
	private final Long token;
}
