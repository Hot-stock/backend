package com.bjcareer.userservice.in.api.response;

import lombok.Data;

@Data
public class MobileAuthenticationResponseDTO {
	private final Long token;
	private final String telegramId;
}
