package com.bjcareer.userservice.application.auth.token;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.RoleType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

class JWTManagerServiceTest {
	@InjectMocks
	private JWTManagerService jwtManagerService;

	private String secretKey = "WUyQS4wsRsm5tiwG49Etyj_Vp3g1kgaEAKZuXwz8sqc=";

	private final String sessionId = "testSessionId";
	private final List<RoleType> roles = Arrays.asList(RoleType.USER);

	@BeforeEach
	void setUp() {
		jwtManagerService = new JWTManagerService(secretKey);
	}

	@Test
	void generateToken_shouldReturnValidJwtTokenVO() {
		// Act
		JwtTokenVO jwtTokenVO = jwtManagerService.generateToken(sessionId, roles);

		// Assert
		assertNotNull(jwtTokenVO.getAccessToken(), "Access token should not be null");
		assertNotNull(jwtTokenVO.getRefreshToken(), "Refresh token should not be null");
		assertEquals(sessionId, jwtTokenVO.getSessionId(), "Session ID should match");
		assertTrue(jwtTokenVO.getRefreshTokenExpireTime() > System.currentTimeMillis(),
			"Refresh token expiration should be in the future");
	}

	@Test
	void verifyToken_shouldReturnClaims_whenTokenIsValid() {
		// Arrange
		String token = jwtManagerService.generateToken(UUID.randomUUID().toString(), roles).getAccessToken();

		// Act
		Claims claims = jwtManagerService.verifyToken(token);

		// Assert
		assertNotNull(claims, "Claims should not be null");
	}

	@Test
	void verifyToken_shouldThrowException_whenTokenIsInvalid() {
		// Arrange
		String invalidToken = "invalidToken";

		// Assert
		assertThrows(MalformedJwtException.class, () -> jwtManagerService.verifyToken(invalidToken),
			"Invalid token should throw SignatureException");
	}

	@Test
	void validateRefreshTokenExpiration_shouldReturnTrue_whenRefreshTokenIsExpired() {
		assertThrows(ExpiredJwtException.class,
			() -> jwtManagerService.verifyToken(generateToken(System.currentTimeMillis() - 1000)),
			"Expired token should throw ExpiredJwtException");
	}

	@Test
	void validateRefreshTokenExpiration_shouldReturnFalse_whenRefreshTokenIsNotExpired() {
		JwtTokenVO jwtTokenVO = jwtManagerService.generateToken(sessionId, roles);
		boolean isExpired = jwtManagerService.validateRefreshTokenExpiration(jwtTokenVO.getRefreshToken());
		assertFalse(isExpired, "Non-expired token should return false");
	}

	private String generateToken(long expiration) {
		Date expirationDate = new Date(expiration);
		return Jwts.builder()
			.subject(UUID.randomUUID().toString())
			.issuedAt(new Date())
			.expiration(expirationDate)
			.signWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey)))
			.compact();
	}
}
