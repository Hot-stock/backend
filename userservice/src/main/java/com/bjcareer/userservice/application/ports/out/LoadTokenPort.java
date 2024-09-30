package com.bjcareer.userservice.application.ports.out;

import java.util.Optional;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.auth.token.valueObject.TokenVO;

public interface LoadTokenPort {
	Optional<JwtTokenVO> findTokenBySessionId(String sessionId);
	Optional<TokenVO> loadByTelemgramId(String token);
}
