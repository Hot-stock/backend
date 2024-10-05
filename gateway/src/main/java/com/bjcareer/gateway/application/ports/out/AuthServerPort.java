package com.bjcareer.gateway.application.ports.out;

import com.bjcareer.gateway.application.ports.in.LogoutCommand;
import com.bjcareer.gateway.application.ports.in.TokenRefreshCommand;
import com.bjcareer.gateway.domain.JWTDomain;

public interface AuthServerPort {
	JWTDomain login(LoginCommandPort loginCommand);
	boolean logout(LogoutCommand loginCommand);
	JWTDomain refresh(TokenRefreshCommand command);
}
