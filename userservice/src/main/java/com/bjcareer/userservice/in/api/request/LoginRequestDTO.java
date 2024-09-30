package com.bjcareer.userservice.in.api.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
	private final String id;
	private final String password;
}
