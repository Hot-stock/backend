package com.bjcareer.userservice.application.ports.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogoutCommand {
	private final String sessionId;
	private final String accessToken;
}
