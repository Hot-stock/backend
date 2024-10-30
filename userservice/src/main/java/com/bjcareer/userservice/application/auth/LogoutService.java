package com.bjcareer.userservice.application.auth;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.in.LogoutCommand;
import com.bjcareer.userservice.application.ports.in.LogoutUsecase;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.RemoveTokenPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUsecase {
	private final LoadTokenPort loadTokenPort;
	private final RemoveTokenPort removeTokenPort;

	@Override
	public Optional<JwtTokenVO> logout(LogoutCommand command) {
		Optional<JwtTokenVO> tokenBySessionId = loadTokenPort.findTokenBySessionId(command.getSessionId());

		if (tokenBySessionId.isPresent()) {
			removeTokenPort.removeToken(command.getSessionId());
		}

		return tokenBySessionId;
	}
}
