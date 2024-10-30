package com.bjcareer.userservice.in.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDTO {
	private String email;
	private String password;

	public LoginRequestDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
