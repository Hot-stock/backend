package com.bjcareer.gateway.application.ports.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterCommand {
	private final String email;
	private final String password;
}
