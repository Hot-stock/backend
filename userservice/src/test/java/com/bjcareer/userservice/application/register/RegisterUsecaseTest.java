package com.bjcareer.userservice.application.register;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.userservice.application.ports.in.RegisterUsecase;
import com.bjcareer.userservice.commonTest.UsecaseTest;
import com.bjcareer.userservice.domain.entity.User;

@UsecaseTest
@SpringBootTest
class RegisterUsecaseTest {
	@Autowired
	RegisterUsecase registerUsecase;

	@Test
	void testGenerateRandomTokenForAuthentication() {
		String telegramId = "testId";
		Long generatedNuber = registerUsecase.generateRandomTokenForAuthentication(telegramId);
		assertNotNull(generatedNuber);
	}

	@Test
	void testRegisterService() {
		String alias = "test";
		String password = "test";
		String telegramId = "telegramId";

		User user = new User(alias, password, telegramId);
		Long s = registerUsecase.registerService(user);
		assertNotNull(s);
	}
}
