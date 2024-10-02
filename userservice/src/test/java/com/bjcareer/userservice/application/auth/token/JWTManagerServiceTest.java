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
import com.bjcareer.userservice.application.auth.token.valueObject.TokenValidationResult;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.domain.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

class JWTManagerServiceTest {
	private JWTUtil jwtUtil;

	private String secretKey = "WUyQS4wsRsm5tiwG49Etyj_Vp3g1kgaEAKZuXwz8sqc=";

	private final String email = "test@test.com";
	private final String sessionId = "testSessionId";
	private final List<RoleType> roles = Arrays.asList(RoleType.USER);

	@BeforeEach
	void setUp() {
		jwtUtil = new JWTUtil(secretKey);
	}

	@Test
	void generateToken_shouldReturnValidJwtTokenVO() {
		// Act
		JwtTokenVO jwtTokenVO = jwtUtil.generateToken(email, sessionId, roles);

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
		String token = jwtUtil.generateToken(email, UUID.randomUUID().toString(), roles).getAccessToken();
		// Assert
		assertTrue(jwtUtil.validateToken(token).isValid(), "Valid token should return true");
	}

	@Test
	void verifyToken_shouldThrowException_whenTokenIsInvalid() {
		// Arrange
		String invalidToken = "invalidToken";

		TokenValidationResult result = jwtUtil.validateToken(invalidToken);
		assertFalse(result.isValid(), "Invalid token should return false");
	}

	@Test
	void validateRefreshTokenExpiration_shouldReturnTrue_whenRefreshTokenIsExpired() {
		User user = new User(email, "password");
		String token = generateToken(user, System.currentTimeMillis() - 1000);
		boolean isExpired = jwtUtil.validateToken(token).isExpired();

		assertTrue(isExpired, "Expired token should return true");
	}

	private String generateToken(User user, long expiration) {
		Date expirationDate = new Date(expiration);
		return Jwts.builder()
			.subject(user.getEmail())
			.issuedAt(new Date())
			.expiration(expirationDate)
			.signWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey)))
			.compact();
	}
}
