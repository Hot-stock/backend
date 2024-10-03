package com.bjcareer.userservice.application.auth.ports;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.in.LoginCommand;
import com.bjcareer.userservice.application.ports.in.LoginUsecase;
import com.bjcareer.userservice.application.ports.in.TokenRefreshCommand;
import com.bjcareer.userservice.application.ports.in.TokenUsecase;
import com.bjcareer.userservice.commonTest.UsecaseTest;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.out.persistance.repository.ActiveUserRepository;
import com.bjcareer.userservice.out.persistance.repository.DatabaseRepository;

@UsecaseTest
@SpringBootTest
@Transactional
class AuthWithTokenUsecaseTest {
	@Autowired
	LoginUsecase loginUsecase;
	@Autowired
	TokenUsecase jwtTokenUsecase;
	@Autowired
	private DatabaseRepository databaseRepository;

	@Autowired
	private ActiveUserRepository activeUserRepository;

	private User testUser;

	@BeforeEach
	void setUp() {
		testUser = new User("testUser", "testPassword");
		databaseRepository.save(testUser);
	}

	@Test
	void shouldLoginSuccessfully() {
		// Arrange
		LoginCommand command = new LoginCommand("testUser", "testPassword");
		LocalDate localDate = LocalDate.now();
		// Act
		User login = loginUsecase.login(command);
		JwtTokenVO jwtTokenVO = jwtTokenUsecase.generateJWT(login);
		activeUserRepository.findByUserInLocalDate(login, localDate);

		// Assert
		assertNotNull(jwtTokenVO);
	}

	@Test
	void shouldFailLoginWithInvalidCredentials() {
		// Arrange
		LoginCommand invalidCommand = new LoginCommand("invalidUser", "wrongPassword");

		// Act & Assert
		assertThrows(UnauthorizedAccessAttemptException.class, () -> loginUsecase.login(invalidCommand));
	}

	@Test
	void shouldRemoveAllTokensWhenTokenTheftIsDetected() {  // 메서드 이름 수정
		// Arrange
		LoginCommand command = new LoginCommand("testUser", "testPassword");
		User login = loginUsecase.login(command);
		JwtTokenVO jwtToken = jwtTokenUsecase.generateJWT(login);

		// Act & Assert
		assertThrows(UnauthorizedAccessAttemptException.class, () -> jwtTokenUsecase.generateAccessTokenViaRefresh(
			new TokenRefreshCommand(jwtToken.getSessionId(), jwtToken.getRefreshToken())));
	}
}
