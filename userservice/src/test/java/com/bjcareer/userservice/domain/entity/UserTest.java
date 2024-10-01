package com.bjcareer.userservice.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {
	final String email = "test@gmail.com";
	final String password = "password";

	@Test
	void slat_생성_유무_검증() {
		User user = new User(email, password);
		assertNotNull(user.getSalt());
	}

	@Test
	void 같은_비밀번호여도_다른_패스워드인지() {
		User user = new User(email, password);
		User user1 = new User("differentEmail", password);

		boolean equals = user1.getPassword().equals(user.getPassword());
		assertFalse(equals);
	}

	@Test
	void 비밀번호_검증() {
		User user = new User(email, password);
		boolean verifyPassword = user.verifyPassword(password);
		assertTrue(verifyPassword);
	}
}
