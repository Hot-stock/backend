package com.bjcareer.userservice.out.persistance.repository;

import java.util.Optional;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.register.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.register.ports.out.SaveTokenPort;

public interface CacheTokenRepository extends LoadTokenPort, SaveTokenPort {
	void saveToken(String key, JwtTokenVO token, Long expirationTime);
	Optional<JwtTokenVO> findTokenBySessionId(String sessionId);
	boolean removeToken(String sessionId);
}
