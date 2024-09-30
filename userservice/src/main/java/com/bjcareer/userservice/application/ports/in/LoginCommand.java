package com.bjcareer.userservice.application.ports.in;

import lombok.Getter;

@Getter
public class LoginCommand {
	private final String id;
	private final String password;

	public LoginCommand(String id, String password) {
		this.id = id;
		this.password = password;
	}
}
