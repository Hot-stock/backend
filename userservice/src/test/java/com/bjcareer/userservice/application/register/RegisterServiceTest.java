package com.bjcareer.userservice.application.register;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bjcareer.userservice.application.ports.out.CreateUserPort;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.LoadUserPort;
import com.bjcareer.userservice.application.ports.out.SaveTokenPort;
import com.bjcareer.userservice.domain.Redis;
import com.bjcareer.userservice.domain.Telegram;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.out.persistance.repository.exceptions.RedisLockAcquisitionException;
import com.bjcareer.userservice.out.persistance.repository.exceptions.TelegramCommunicationException;
import com.bjcareer.userservice.out.persistance.repository.exceptions.UserAlreadyExistsException;

class RegisterServiceTest {
	SaveTokenPort saveTokenPort;
	LoadTokenPort loadTokenPort;
	LoadUserPort loadUserPort;
	CreateUserPort createUserPort;

	Telegram telegram;
	Redis redis;

	RegisterService registerService;

	@BeforeEach
	void setUp() {
		saveTokenPort = mock(SaveTokenPort.class);
		loadTokenPort = mock(LoadTokenPort.class);
		loadUserPort = mock(LoadUserPort.class);
		createUserPort = mock(CreateUserPort.class);

		telegram = mock(Telegram.class);
		redis = mock(Redis.class);

		registerService = new RegisterService(saveTokenPort, loadTokenPort, loadUserPort, createUserPort, telegram,
			redis);
	}

	@Test
	void testGenerateRandomTokenForAuthentication() {
		String telegramId = "test";

		when(telegram.sendCode(eq(telegramId), anyLong())).thenReturn(true);
		Long generatedNuber = registerService.generateRandomTokenForAuthentication(telegramId);
		assertNotNull(generatedNuber);
	}

	@Test
	void when_send_fail() {
		when(telegram.sendCode(anyString(), anyLong())).thenReturn(false);

		assertThrows(TelegramCommunicationException.class, () -> {
			registerService.generateRandomTokenForAuthentication("test");
		});
	}

	@Test
	void testRegisterServiceWhenLockFail() {
		when(redis.tryLock(anyString())).thenReturn(false);

		assertThrows(RedisLockAcquisitionException.class, () -> {
			registerService.registerService(any());
		});
	}

	@Test
	void testRegisterServiceWhenFindUser() {
		String alias = "test";
		String password = "test";
		String telegramId = "telegramId";

		User user = new User(alias, password, telegramId);

		when(redis.tryLock(anyString())).thenReturn(true);
		when(loadUserPort.findByUserAlias(anyString())).thenReturn(Optional.of(user));

		assertThrows(UserAlreadyExistsException.class, () -> {
			registerService.registerService(user);
		});
	}
}
