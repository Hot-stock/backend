package com.bjcareer.gateway.in.api.request;

import lombok.Data;

@Data
public class MobileAuthenticationVerifyRequestDTO {
	private String email;
	private Long token;
}
