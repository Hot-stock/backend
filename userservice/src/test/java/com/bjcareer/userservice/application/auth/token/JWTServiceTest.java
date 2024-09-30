package com.bjcareer.userservice.application.auth.token;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.ports.TonkenManagerUsecase;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.out.persistance.repository.CacheTokenRepository;

class JWTServiceTest {

	@Mock
	private TonkenManagerUsecase tokenManager;

	@Mock
	private CacheTokenRepository tokenRepository;

	@InjectMocks
	private JWTService jwtService;

	private User testUser;
	private JwtTokenVO jwtTokenVO;
	private String sessionId;
	private String refreshToken;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		sessionId = UUID.randomUUID().toString();
		refreshToken = "testRefreshToken";

		testUser = new User("testUser", "testPassword", "testEmail");

		jwtTokenVO = new JwtTokenVO("accessToken", refreshToken, sessionId, 3600000L, testUser.getRoles());
	}

	@Test
	void generateToken_shouldReturnValidJwtTokenVO() {
		// Arrange
		when(tokenManager.generateToken(any(String.class), any())).thenReturn(jwtTokenVO);

		// Act
		JwtTokenVO result = jwtService.generateToken(testUser);

		// Assert
		assertNotNull(result);
		assertEquals("accessToken", result.getAccessToken());
		assertEquals(refreshToken, result.getRefreshToken());
		verify(tokenRepository).saveToken(any(String.class), any(JwtTokenVO.class), anyLong());
	}

	@Test
	void generateAccessTokenViaRefresh_shouldReturnNewAccessToken_whenRefreshTokenValid() {
		// Arrange
		when(tokenRepository.findTokenBySessionId(sessionId)).thenReturn(Optional.of(jwtTokenVO));
		when(tokenManager.validateRefreshTokenExpiration(refreshToken)).thenReturn(true);
		when(tokenManager.generateToken(any(), any())).thenReturn(jwtTokenVO);

		// Act
		JwtTokenVO result = jwtService.generateAccessTokenViaRefresh(sessionId, refreshToken);

		// Assert
		assertNotNull(result);
		verify(tokenRepository).saveToken(any(), any(), anyLong());
	}

	@Test
	void generateAccessTokenViaRefresh_shouldThrowException_whenRefreshTokenInvalid() {
		// Arrange
		when(tokenRepository.findTokenBySessionId(sessionId)).thenReturn(Optional.of(jwtTokenVO));
		when(tokenManager.validateRefreshTokenExpiration(refreshToken)).thenReturn(false);

		// Act & Assert
		UnauthorizedAccessAttemptException exception = assertThrows(UnauthorizedAccessAttemptException.class,
			() -> jwtService.generateAccessTokenViaRefresh(sessionId, refreshToken));
		assertEquals("Token theft is suspected. Please generate a new token.", exception.getMessage());
		verify(tokenRepository).removeToken(sessionId);
	}

	@Test
	void validateSessionAndToken_shouldThrowException_whenSessionInvalid() {
		// Arrange
		when(tokenRepository.findTokenBySessionId(sessionId)).thenReturn(Optional.empty());

		// Act & Assert
		UnauthorizedAccessAttemptException exception = assertThrows(UnauthorizedAccessAttemptException.class,
			() -> jwtService.generateAccessTokenViaRefresh(sessionId, refreshToken));
		assertEquals("Invalid session ID.", exception.getMessage());
	}

	@Test
	void validateSessionAndToken_shouldThrowException_whenTokensDoNotMatch() {
		// Arrange
		JwtTokenVO invalidToken = new JwtTokenVO("accessToken", "differentRefreshToken", sessionId, 3600000L,
			testUser.getRoles());
		when(tokenRepository.findTokenBySessionId(sessionId)).thenReturn(Optional.of(invalidToken));

		// Act & Assert
		UnauthorizedAccessAttemptException exception = assertThrows(UnauthorizedAccessAttemptException.class,
			() -> jwtService.generateAccessTokenViaRefresh(sessionId, refreshToken));
		assertEquals("The token does not match.", exception.getMessage());
	}
}
