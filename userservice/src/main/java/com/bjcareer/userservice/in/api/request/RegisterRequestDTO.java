package com.bjcareer.userservice.in.api.request;

import lombok.Data;

@Data
public class RegisterRequestDTO {
	private final String email;
	private final String password;
}
