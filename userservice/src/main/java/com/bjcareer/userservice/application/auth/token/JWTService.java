package com.bjcareer.userservice.application.auth.token;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.in.TokenUsecase;
import com.bjcareer.userservice.application.ports.in.TonkenManagerUsecase;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.RemoveTokenPort;
import com.bjcareer.userservice.application.ports.out.SaveTokenPort;
import com.bjcareer.userservice.domain.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTService implements TokenUsecase {
	private final TonkenManagerUsecase tokenManager;
	private final SaveTokenPort saveTokenPort;
	private final LoadTokenPort loadTokenPort;
	private final RemoveTokenPort removeTokenPort;

	@Override
	public JwtTokenVO generateToken(User user) {
		String sessionId = UUID.randomUUID().toString();
		JwtTokenVO jwtTokenVO = tokenManager.generateToken(sessionId, user.getRoles());
		saveTokenPort.saveJWT(sessionId, jwtTokenVO, tokenManager.REFRESH_TOKEN_EXPIRE_DURATION_SEC);
		return jwtTokenVO;
	}

	@Override
	public JwtTokenVO generateAccessTokenViaRefresh(String sessionId, String refreshToken) {
		JwtTokenVO jwtTokenVO = validateSessionAndToken(sessionId, refreshToken, true);

		boolean isVerify = tokenManager.validateRefreshTokenExpiration(refreshToken);

		if (!isVerify) {
			boolean isRemoved = removeTokenPort.removeToken(jwtTokenVO.getSessionId());

			if (!isRemoved) {
				log.error("Failed to remove JWT from Redis. Please contact the developer.");
			}
			throw new UnauthorizedAccessAttemptException("Token theft is suspected. Please generate a new token.");
		}

		saveTokenPort.saveJWT(sessionId, jwtTokenVO, jwtTokenVO.getRefreshTokenExpireTime());
		return tokenManager.generateToken(sessionId, jwtTokenVO.getRoleType());
	}

	@Override
	public boolean verifyAccessToken(String accessToken) {
		return false;
	}

	private JwtTokenVO validateSessionAndToken(String sessionId, String token, boolean isRefreshToken) {
		Optional<JwtTokenVO> authToken = loadTokenPort.findTokenBySessionId(sessionId);

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
