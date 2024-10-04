package com.bjcareer.gateway.application.ports.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginCommandPort {
	private final String email;
	private final String password;
}
