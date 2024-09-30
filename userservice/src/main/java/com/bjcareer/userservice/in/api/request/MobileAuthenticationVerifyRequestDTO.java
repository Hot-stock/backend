package com.bjcareer.userservice.in.api.request;

import lombok.Data;

@Data
public class MobileAuthenticationVerifyRequestDTO {
	private final Long token;
	private final String telegramId;
}
