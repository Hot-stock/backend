package com.bjcareer.userservice.application.ports.out.message;

import lombok.Data;

@Data
public class AuthCodeSentEvent {
	private final String email;
	private final Long code;
}
