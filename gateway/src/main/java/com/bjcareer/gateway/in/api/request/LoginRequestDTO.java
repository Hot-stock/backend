package com.bjcareer.gateway.in.api.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
	private String email;
	private String password;
}
