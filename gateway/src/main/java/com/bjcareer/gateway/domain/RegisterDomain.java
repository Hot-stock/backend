package com.bjcareer.gateway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RegisterDomain {
	private final String email;
	private final String password;
	private final Long id;
}
