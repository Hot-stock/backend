package com.bjcareer.userservice.application.token.ports;

import com.bjcareer.userservice.application.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.User;

public interface TokenUsecase {
	JwtTokenVO generateToken(User user);

	JwtTokenVO generateAccessTokenViaRefresh(String sessionId, String refreshToken);

	boolean verifyAccessToken(String accessToken);
}
