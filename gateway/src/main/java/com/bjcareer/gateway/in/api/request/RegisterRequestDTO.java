package com.bjcareer.gateway.in.api.request;

import lombok.Data;

@Data
public class RegisterRequestDTO {
	private String email;
	private String password;
}
