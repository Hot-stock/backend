package com.bjcareer.gateway.application.ports.in;

import lombok.Data;

@Data
public class LoginCommand {
	private final String email;
	private final String password;
}
