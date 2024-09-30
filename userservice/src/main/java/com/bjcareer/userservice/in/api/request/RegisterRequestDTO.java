package com.bjcareer.userservice.in.api.request;

import lombok.Data;

@Data
public class RegisterRequestDTO {
	private final String userId;
	private final String password;
	public final String telegramId;
}
