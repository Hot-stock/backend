package com.bjcareer.userservice.application.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bjcareer.userservice.application.auth.token.JWTUtil;
import com.bjcareer.userservice.application.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.auth.token.valueObject.TokenValidationResult;
import com.bjcareer.userservice.application.ports.in.TokenRefreshCommand;
import com.bjcareer.userservice.application.ports.in.TokenUsecase;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.RemoveTokenPort;
import com.bjcareer.userservice.application.ports.out.SaveTokenPort;
import com.bjcareer.userservice.domain.entity.User;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTService implements TokenUsecase {
	private final JWTUtil jwtUtil;
	private final SaveTokenPort saveTokenPort;
	private final LoadTokenPort loadTokenPort;
	private final RemoveTokenPort removeTokenPort;

	@Override
	public JwtTokenVO generateJWT(User user) {
		String sessionId = UUID.randomUUID().toString();
		JwtTokenVO jwtTokenVO = jwtUtil.generateToken(user.getEmail(), sessionId, user.getRoles());
		saveTokenPort.saveJWT(sessionId, jwtTokenVO, JWTUtil.REFRESH_TOKEN_EXPIRE_DURATION_SEC);
		return jwtTokenVO;
	}

	@Override
	public JwtTokenVO generateAccessTokenViaRefresh(TokenRefreshCommand command) {
		JwtTokenVO jwtTokenVO = validateSessionAndToken(command.getSessionId(), command.getRefreshToken(), true);
		TokenValidationResult tokenValidationResult = jwtUtil.validateToken(jwtTokenVO.getAccessToken());

		if (!tokenValidationResult.isExpired()) {
			boolean isRemoved = removeTokenPort.removeToken(jwtTokenVO.getSessionId());

			if (!isRemoved) {
				log.error("Failed to remove JWT from Redis. Please contact the developer.");
			}

			throw new UnauthorizedAccessAttemptException("Token theft is suspected. Please generate a new token.");
		}

		Claims claims = jwtUtil.parseToken(command.getRefreshToken());
		String subject = claims.getSubject();

		saveTokenPort.saveJWT(command.getSessionId(), jwtTokenVO, JWTUtil.REFRESH_TOKEN_EXPIRE_DURATION_SEC);
		return jwtUtil.generateToken(subject, command.getSessionId(), jwtTokenVO.getRoleType());
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
