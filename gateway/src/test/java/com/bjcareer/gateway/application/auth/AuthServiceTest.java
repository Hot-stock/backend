package com.bjcareer.gateway.application.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.gateway.application.ports.in.LoginCommand;
import com.bjcareer.gateway.domain.JWTDomain;
import com.bjcareer.gateway.exceptions.UnauthorizedAccessAttemptException;

@SpringBootTest
class AuthServiceTest {
	@Autowired AuthService authService;

	@Test
	void loginUsecase() {
		LoginCommand loginCommand = new LoginCommand("wodhksqw@naver.com", "friend77asd@");
		JWTDomain JWTDomain = authService.loginUsecase(loginCommand);

		assertNotNull(JWTDomain.getAccessToken());
		assertNotNull(JWTDomain.getRefreshToken());
		assertNotNull(JWTDomain.getSessionId());
	}

	@Test
	void loginFailcasee() {
		LoginCommand loginCommand = new LoginCommand("wodhksqw2@naver.com", "friend77asd@");
		assertThrows(UnauthorizedAccessAttemptException.class, () -> authService.loginUsecase(loginCommand));
	}

	@Test
	void logoutUsercase() {
		fail("Not yet implemented");
	}
}
