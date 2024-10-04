package com.bjcareer.gateway.application.ports.out;

import com.bjcareer.gateway.application.ports.in.LogoutCommand;
import com.bjcareer.gateway.domain.LoginResponseDomain;

public interface AuthServerPort {
	LoginResponseDomain login(LoginCommandPort loginCommand);
	boolean logout(LogoutCommand loginCommand);
}
