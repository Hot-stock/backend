package com.bjcareer.gateway.application.auth;

import org.springframework.stereotype.Service;

import com.bjcareer.gateway.application.ports.in.AuthUsecase;
import com.bjcareer.gateway.application.ports.in.LoginCommand;
import com.bjcareer.gateway.application.ports.in.LogoutCommand;
import com.bjcareer.gateway.application.ports.in.TokenRefreshCommand;
import com.bjcareer.gateway.application.ports.out.AuthServerPort;
import com.bjcareer.gateway.application.ports.out.LoginCommandPort;
import com.bjcareer.gateway.domain.JWTDomain;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUsecase {
	private final AuthServerPort port;

	@Override
	public JWTDomain loginUsecase(LoginCommand loginCommand) {
		LoginCommandPort loginCommandPort = new LoginCommandPort(loginCommand.getEmail(), loginCommand.getPassword());
		return port.login(loginCommandPort);
	}

	@Override
	public boolean logoutUsercase(LogoutCommand logoutCommand) {
		return port.logout(logoutCommand);
	}

	@Override
	public JWTDomain refreshUsecase(TokenRefreshCommand command) {
		return port.refresh(command);
	}
}