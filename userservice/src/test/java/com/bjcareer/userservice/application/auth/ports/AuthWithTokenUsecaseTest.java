package com.bjcareer.userservice.application.auth.ports;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.in.AuthWithTokenUsecase;
import com.bjcareer.userservice.application.ports.in.LoginCommand;
import com.bjcareer.userservice.application.ports.in.TokenRefreshCommand;
import com.bjcareer.userservice.commonTest.UsecaseTest;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.out.persistance.repository.DatabaseRepository;

@UsecaseTest
@SpringBootTest
@Transactional
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
	void shouldRemoveAllTokensWhenTokenTheftIsDetected() {  // 메서드 이름 수정
		// Arrange
		LoginCommand command = new LoginCommand("testUser", "testPassword");
		JwtTokenVO jwtToken = authWithTokenUsecase.login(command);

		// Act & Assert
		assertThrows(UnauthorizedAccessAttemptException.class, () -> authWithTokenUsecase.generateAccessTokenViaRefresh(
			new TokenRefreshCommand(jwtToken.getSessionId(), jwtToken.getRefreshToken())));
	}
}
