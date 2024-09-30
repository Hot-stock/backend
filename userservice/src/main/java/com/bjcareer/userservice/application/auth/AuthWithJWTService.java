package com.bjcareer.userservice.application.auth;

import org.springframework.stereotype.Service;

import com.bjcareer.userservice.application.ports.in.LoginCommand;
import com.bjcareer.userservice.application.ports.in.LoginUsecase;
import com.bjcareer.userservice.application.ports.in.AuthWithTokenUsecase;
import com.bjcareer.userservice.application.ports.in.TokenRefreshCommand;
import com.bjcareer.userservice.application.ports.in.TokenUsecase;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthWithJWTService implements AuthWithTokenUsecase {
	private final LoginUsecase loginUsecase;
	private final TokenUsecase tokenUsecase;

	@Override
	public JwtTokenVO login(LoginCommand loginCommand) {
		LoginCommand command = new LoginCommand(loginCommand.getEmail(), loginCommand.getPassword());
		User user = loginUsecase.login(command);
		return tokenUsecase.generateToken(user);
	}

	@Override
	public JwtTokenVO generateAccessTokenViaRefresh(TokenRefreshCommand tokenRefreshCommand) {
		return tokenUsecase.generateAccessTokenViaRefresh(tokenRefreshCommand.getSessionId(), tokenRefreshCommand.getRefreshToken());
	}
}
