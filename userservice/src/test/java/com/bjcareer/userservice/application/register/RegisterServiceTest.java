package com.bjcareer.userservice.application.register;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bjcareer.userservice.application.auth.token.valueObject.TokenVO;
import com.bjcareer.userservice.domain.Redis;
import com.bjcareer.userservice.domain.Telegram;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.exceptions.RedisLockAcquisitionException;
import com.bjcareer.userservice.exceptions.TelegramCommunicationException;
import com.bjcareer.userservice.exceptions.UserAlreadyExistsException;
import com.bjcareer.userservice.repository.DatabaseRepository;
import com.bjcareer.userservice.repository.RedisRepository;

class RegisterServiceTest {
	RegisterService registerService;
	RedisRepository redisRepository;
	Telegram telegram;
	Redis redis;
	DatabaseRepository databaseRepository;

	@BeforeEach
	void setUp() {
		databaseRepository = mock(DatabaseRepository.class);
		redisRepository = mock(RedisRepository.class);
		telegram = mock(Telegram.class);
		redis = mock(Redis.class);

		registerService = new RegisterService(databaseRepository, redisRepository, telegram, redis);
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
	void testVerifySameToken() {
		String telegramId = "testID";
		Long token = 1234L;
		TokenVO tokenVO = new TokenVO(telegramId, token);

		when(redisRepository.findTokenByTelegramId(telegramId)).thenReturn(Optional.of(tokenVO));
		boolean b = registerService.verifyToken(telegramId, token);

		assertTrue(registerService.verifyToken(telegramId, token));
	}

	@Test
	void testVerifyDifferentToken() {
		String telegramId = "testID";
		Long token = 1234L;
		Long diffToken = 12341L;

		TokenVO diffTokenVO = new TokenVO(telegramId, diffToken);

		when(redisRepository.findTokenByTelegramId(telegramId)).thenReturn(Optional.of(diffTokenVO));

		assertFalse(registerService.verifyToken(telegramId, token));
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
		when(databaseRepository.findByUserId(anyString())).thenReturn(Optional.of(user));

		assertThrows(UserAlreadyExistsException.class, () -> {
			registerService.registerService(user);
		});
	}
}
