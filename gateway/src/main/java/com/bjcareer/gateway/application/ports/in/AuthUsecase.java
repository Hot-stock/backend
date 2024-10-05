package com.bjcareer.gateway.application.ports.in;

import com.bjcareer.gateway.domain.JWTDomain;

public interface AuthUsecase {
	JWTDomain loginUsecase(LoginCommand loginCommand);
	boolean logoutUsercase(LogoutCommand logoutCommand);
	JWTDomain refreshUsecase(TokenRefreshCommand command);
}
