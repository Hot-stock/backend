package com.bjcareer.userservice.in.api.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequestDTO {
	private String email;
	private String password;

	public RegisterRequestDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
