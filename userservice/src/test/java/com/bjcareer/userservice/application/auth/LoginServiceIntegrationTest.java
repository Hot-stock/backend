package com.bjcareer.userservice.application.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.ports.in.LoginCommand;
import com.bjcareer.userservice.application.ports.out.CreateUserPort;
import com.bjcareer.userservice.domain.entity.User;

@SpringBootTest
@Transactional
class LoginServiceIntegrationTest {
	@Autowired
	LoginService loginService;

	@Autowired
	CreateUserPort userPort;

	@Test
	void 로그인실패_회원가입안함() {
		LoginCommand command = new LoginCommand("test", "test");
		assertThrows(UnauthorizedAccessAttemptException.class, () -> loginService.login(command));
	}

	@Test
	@Transactional
	void 로그인실패_패스워드_불일치() {
		String email = "test12@asd.com";
		String password = "test";
		String wrongPassword = "test1";

		LoginCommand command = new LoginCommand(email, wrongPassword);
		User user = new User(email, password);

		userPort.save(user);
		assertThrows(UnauthorizedAccessAttemptException.class, () -> loginService.login(command));
	}

	@Test
	@Transactional
	void 로그인_성공() {
		String email = "test12@asd.com";
		String password = "test";
		LoginCommand command = new LoginCommand(email, password);

		User user = new User(email, password);
		userPort.save(user);

		User login = loginService.login(command);
		assertNotNull(login);
	}

}
