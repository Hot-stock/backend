package com.bjcareer.userservice.in.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDTO {
	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	public LoginRequestDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
