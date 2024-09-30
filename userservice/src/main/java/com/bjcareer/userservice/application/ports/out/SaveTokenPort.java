package com.bjcareer.userservice.application.ports.out;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.auth.token.valueObject.TokenVO;

public interface SaveTokenPort {
	void saveJWT(String key, JwtTokenVO token, Long expirationTime);
	void saveAuthToken(TokenVO token, Long expirationTime);
}
