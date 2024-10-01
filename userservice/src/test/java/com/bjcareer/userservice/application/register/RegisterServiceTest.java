package com.bjcareer.userservice.application.register;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import com.bjcareer.userservice.application.auth.token.valueObject.TokenVO;
import com.bjcareer.userservice.application.ports.out.CreateUserPort;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.LoadUserPort;
import com.bjcareer.userservice.application.ports.out.SaveTokenPort;
import com.bjcareer.userservice.domain.Redis;

class RegisterServiceTest {
	@Mock
	SaveTokenPort saveTokenPort;
	@Mock
	LoadTokenPort loadToken;
	@Mock
	LoadUserPort loadUserPort;
	@Mock
	CreateUserPort createUserPort;
	@Mock
	ApplicationEventPublisher eventPublisher;
	@Mock
	Redis redisDomain;

	@InjectMocks
	RegisterService registerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void 토큰값_검증이_제대로_동작하는지() {
		String email = "test@test.com";
		Long token = 123456L;
		TokenVO tokenVO = new TokenVO(email, token);

		when(loadToken.loadVerificationTokenByEmail(email)).thenReturn(Optional.of(tokenVO));

		boolean result = registerService.verifyToken(email, token);
		assertTrue(result);
	}
}
