package com.bjcareer.userservice.application.auth.token.ports;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.userservice.application.auth.token.JWTUtil;
import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.auth.token.valueObject.TokenValidationResult;
import com.bjcareer.userservice.application.ports.in.TokenRefreshCommand;
import com.bjcareer.userservice.application.ports.in.TokenUsecase;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.SaveTokenPort;
import com.bjcareer.userservice.commonTest.UsecaseTest;
import com.bjcareer.userservice.domain.entity.RoleType;

@UsecaseTest
@SpringBootTest
class TokenManagerUsecaseTest {
	@Autowired
	JWTUtil tokenManagerUsecase;
	String email = "test@test.com";
	@Autowired
	TokenUsecase tokenUsecase;

	@Autowired
	LoadTokenPort loadTokenPort;

	@Autowired
	SaveTokenPort saveTokenPort;

	@Test
	void testGenerateToken() {
		String sessionId = UUID.randomUUID().toString();
		List<RoleType> roles = List.of(RoleType.USER);
		JwtTokenVO jwtTokenVO = tokenManagerUsecase.generateToken(email, sessionId, roles);
		assertNotNull(jwtTokenVO);
	}

	@Test
	void verifyToken() {
		String sessionId = UUID.randomUUID().toString();
		List<RoleType> roles = List.of(RoleType.USER);
		JwtTokenVO jwtTokenVO = tokenManagerUsecase.generateToken(email, sessionId, roles);

		TokenValidationResult tokenValidationResult = tokenManagerUsecase.validateToken(jwtTokenVO.getAccessToken());
		assertTrue(tokenValidationResult.isValid());
	}

	@Test
	void 엑세스토큰_만료전_재갱신_요청이_오면_모든_토큰_자동_폐기() {
		String sessionId = UUID.randomUUID().toString();
		List<RoleType> roles = List.of(RoleType.USER);
		JwtTokenVO jwtTokenVO = tokenManagerUsecase.generateToken(email, sessionId, roles);
		TokenValidationResult tokenValidationResult = tokenManagerUsecase.validateToken(jwtTokenVO.getAccessToken());
		assertTrue(tokenValidationResult.isValid());

		saveTokenPort.saveJWT(sessionId, jwtTokenVO, JWTUtil.REFRESH_TOKEN_EXPIRE_DURATION_SEC);
		Optional<JwtTokenVO> tokenBySessionId = loadTokenPort.findTokenBySessionId(sessionId);
		assertTrue(tokenBySessionId.isPresent());

		assertThrows(UnauthorizedAccessAttemptException.class, () -> {
			tokenUsecase.generateAccessTokenViaRefresh(new TokenRefreshCommand(sessionId, jwtTokenVO.getRefreshToken()));
		});

		tokenBySessionId = loadTokenPort.findTokenBySessionId(sessionId);
		assertTrue(tokenBySessionId.isEmpty());
	}
}
