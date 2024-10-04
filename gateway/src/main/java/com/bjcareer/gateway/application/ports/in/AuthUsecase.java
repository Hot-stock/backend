package com.bjcareer.gateway.application.ports.in;

import com.bjcareer.gateway.domain.LoginResponseDomain;

public interface AuthUsecase {
	LoginResponseDomain loginUsecase(LoginCommand loginCommand);
	boolean logoutUsercase(LogoutCommand logoutCommand);
}
