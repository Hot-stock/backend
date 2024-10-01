package com.bjcareer.userservice.in.api.request;

import lombok.Data;

@Data
public class RegisterRequestDTO {
	private String email;
	private String password;
}
