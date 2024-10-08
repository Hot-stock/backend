package com.bjcareer.gateway.application.ports.out;

import com.bjcareer.gateway.application.ports.in.LoginCommand;
import com.bjcareer.gateway.application.ports.in.LogoutCommand;
import com.bjcareer.gateway.application.ports.in.TokenRefreshCommand;
import com.bjcareer.gateway.domain.JWTDomain;

public interface AuthServerPort {
	JWTDomain login(LoginCommand loginCommand);
	boolean logout(LogoutCommand logoutCommand);
	JWTDomain refresh(TokenRefreshCommand command);
}
