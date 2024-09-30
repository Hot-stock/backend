package com.bjcareer.userservice.application.auth.ports;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.commonTest.UsecaseTest;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.repository.DatabaseRepository;

@UsecaseTest
@SpringBootTest
class AuthWithTokenUsecaseTest {

	@Autowired
	private AuthWithTokenUsecase authWithTokenUsecase;
	@Autowired
	private DatabaseRepository databaseRepository;
	private User testUser;

	@BeforeEach
	void setUp() {
		testUser = new User("testUser", "testPassword", "testEmail");
		databaseRepository.save(testUser);
	}

	@Test
	void shouldLoginSuccessfully() {
		// Arrange
		LoginCommand command = new LoginCommand("testUser", "testPassword");

		// Act
		JwtTokenVO jwtToken = authWithTokenUsecase.login(command);

		// Assert
		assertNotNull(jwtToken);
	}

	@Test
	void shouldFailLoginWithInvalidCredentials() {
		// Arrange
		LoginCommand invalidCommand = new LoginCommand("invalidUser", "wrongPassword");

		// Act & Assert
		assertThrows(UnauthorizedAccessAttemptException.class, () -> authWithTokenUsecase.login(invalidCommand));
	}

	@Test
	void shouldGenerateNewAccessTokenViaRefresh() {
		// Arrange
		LoginCommand command = new LoginCommand("testUser", "testPassword");
		JwtTokenVO jwtToken = authWithTokenUsecase.login(command);

		// Act
		JwtTokenVO refreshedToken = authWithTokenUsecase.generateAccessTokenViaRefresh(
			new TokenRefreshCommand(jwtToken.getSessionId(), jwtToken.getRefreshToken()));

		// Assert
		assertAll(
			() -> assertNotEquals(jwtToken.getAccessToken(), refreshedToken.getAccessToken(),
				"Access tokens should differ."),
			() -> assertNotEquals(jwtToken.getRefreshToken(), refreshedToken.getRefreshToken(),
				"Refresh tokens should differ."),
			() -> assertNotEquals(jwtToken.getRefreshTokenExpireTime(), refreshedToken.getRefreshTokenExpireTime(),
				"Refresh token expiry should be updated."),
			() -> assertEquals(jwtToken.getSessionId(), refreshedToken.getSessionId(), "Session IDs should match."),
			() -> assertEquals(jwtToken.getRoleType(), refreshedToken.getRoleType(), "Role types should match.")
		);
	}
}
