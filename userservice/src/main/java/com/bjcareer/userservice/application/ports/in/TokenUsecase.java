package com.bjcareer.userservice.application.ports.in;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.User;

public interface TokenUsecase {
	JwtTokenVO generateToken(User user);
	JwtTokenVO generateAccessTokenViaRefresh(String sessionId, String refreshToken);
	boolean verifyAccessToken(String accessToken);
}
