package com.bjcareer.gateway.application.ports.in;

import lombok.Data;

@Data
public class LogoutCommand {
	private final String sessionId;
	private final String accessToken;
}
