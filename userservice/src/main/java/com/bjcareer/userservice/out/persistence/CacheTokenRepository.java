package com.bjcareer.userservice.out.persistence;

import java.util.Optional;

import com.bjcareer.userservice.application.token.valueObject.JwtTokenVO;

public interface CacheTokenRepository {
	void saveToken(String key, JwtTokenVO token, Long expirationTime);

	Optional<JwtTokenVO> findTokenBySessionId(String sessionId);

	boolean removeToken(String sessionId);

}
