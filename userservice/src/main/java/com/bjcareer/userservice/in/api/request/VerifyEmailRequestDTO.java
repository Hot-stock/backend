package com.bjcareer.userservice.in.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyEmailRequestDTO {
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email should not be empty")
	private String email;

	public VerifyEmailRequestDTO(String email) {
		this.email = email;
	}
}
