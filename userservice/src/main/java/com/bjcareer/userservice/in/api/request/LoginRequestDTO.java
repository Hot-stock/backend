package com.bjcareer.userservice.in.api.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
	private String email;
	private String password;
}
