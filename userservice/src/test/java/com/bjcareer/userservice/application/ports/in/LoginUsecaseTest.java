package com.bjcareer.userservice.application.ports.in;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.domain.entity.User;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class LoginUsecaseTest {
	@Autowired EntityManager entityManager;
	@Autowired LoginUsecase loginUsecase;

	@Test
	void test_login_시도_성공() {
		String username = "testUser";
		String password = "testPassword";
		User user = new User(username, password);
		entityManager.persist(user);

		User login = loginUsecase.login(new LoginCommand(username, password));
		assertEquals(user, login);
	}
	@Test
	void test_login_시도_실패() {
		String username = "testUser";
		String password = "testPassword";
		String inputPassword = "testPassword2";

		User user = new User(username, password);
		entityManager.persist(user);

		assertThrows(UnauthorizedAccessAttemptException.class, () -> {
			loginUsecase.login(new LoginCommand(username, inputPassword));
		});
	}
}
