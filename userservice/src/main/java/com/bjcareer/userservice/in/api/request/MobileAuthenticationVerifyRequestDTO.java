package com.bjcareer.userservice.in.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MobileAuthenticationVerifyRequestDTO {
	private String email;
	private Long token;

	public MobileAuthenticationVerifyRequestDTO(String email, Long token) {
		this.email = email;
		this.token = token;
	}
}
