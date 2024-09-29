package com.bjcareer.userservice.application.auth.ports;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;

public interface AuthWithTokenUsecase {
	JwtTokenVO login(LoginCommand loginCommand);
	JwtTokenVO generateAccessTokenViaRefresh(TokenRefreshCommand tokenRefreshCommand);
}
