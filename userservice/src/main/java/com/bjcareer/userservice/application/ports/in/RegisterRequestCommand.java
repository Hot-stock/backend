package com.bjcareer.userservice.application.ports.in;

import lombok.Data;

@Data
public class RegisterRequestCommand {
	private final String email;
	private final String password;

	public RegisterRequestCommand(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
