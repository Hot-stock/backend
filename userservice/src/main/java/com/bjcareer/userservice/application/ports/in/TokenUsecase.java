package com.bjcareer.userservice.application.ports.in;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.User;

public interface TokenUsecase {
	JwtTokenVO generateJWT(User user);
	JwtTokenVO generateAccessTokenViaRefresh(TokenRefreshCommand command);
	boolean verifyAccessToken(String accessToken);
}
