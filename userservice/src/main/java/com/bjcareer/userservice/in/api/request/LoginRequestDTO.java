package com.bjcareer.userservice.in.api.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
	private String id;
	private String password;
}
