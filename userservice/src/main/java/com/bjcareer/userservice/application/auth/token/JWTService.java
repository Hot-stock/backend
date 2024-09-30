package com.bjcareer.userservice.application.auth.token;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.ports.TokenUsecase;
import com.bjcareer.userservice.application.auth.token.ports.TonkenManagerUsecase;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.out.persistance.repository.CacheTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTService implements TokenUsecase {
	private final TonkenManagerUsecase tokenManager;
	private final CacheTokenRepository tokenRepository;

	@Override
	public JwtTokenVO generateToken(User user) {
		String sessionId = UUID.randomUUID().toString();
		JwtTokenVO jwtTokenVO = tokenManager.generateToken(sessionId, user.getRoles());
		tokenRepository.saveToken(sessionId, jwtTokenVO, tokenManager.REFRESH_TOKEN_EXPIRE_DURATION_SEC);
		return jwtTokenVO;
	}

	@Override
	public JwtTokenVO generateAccessTokenViaRefresh(String sessionId, String refreshToken) {
		JwtTokenVO jwtTokenVO = validateSessionAndToken(sessionId, refreshToken, true);

		boolean isVerify = tokenManager.validateRefreshTokenExpiration(refreshToken);

		if (!isVerify) {
			boolean isRemoved = tokenRepository.removeToken(jwtTokenVO.getSessionId());

			if (!isRemoved) {
				log.error("Failed to remove JWT from Redis. Please contact the developer.");
			}
			throw new UnauthorizedAccessAttemptException("Token theft is suspected. Please generate a new token.");
		}

		tokenRepository.saveToken(sessionId, jwtTokenVO, jwtTokenVO.getRefreshTokenExpireTime());

		return tokenManager.generateToken(sessionId, jwtTokenVO.getRoleType());
	}

	@Override
	public boolean verifyAccessToken(String accessToken) {
		return false;
	}

	private JwtTokenVO validateSessionAndToken(String sessionId, String token, boolean isRefreshToken) {
		Optional<JwtTokenVO> authToken = tokenRepository.findTokenBySessionId(sessionId);

		if (authToken.isEmpty()) {
			throw new UnauthorizedAccessAttemptException("Invalid session ID.");
		}

		JwtTokenVO jwtTokenVO = authToken.get();

		if ((isRefreshToken && !jwtTokenVO.getRefreshToken().equals(token)) ||
			(!isRefreshToken && !jwtTokenVO.getAccessToken().equals(token))) {
			throw new UnauthorizedAccessAttemptException("The token does not match.");
		}

		return authToken.get();
	}

}
