package com.bjcareer.userservice.application.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.in.LogoutCommand;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.RemoveTokenPort;

class LogoutServiceTest {
	LoadTokenPort loadTokenPort;
	LogoutService logoutService;
	RemoveTokenPort removeTokenPort;

	@BeforeEach
	void setUp() {
		loadTokenPort = mock(LoadTokenPort.class);
		removeTokenPort = mock(RemoveTokenPort.class);
		logoutService = new LogoutService(loadTokenPort, removeTokenPort);
	}

	@Test
	void 세션아이디가_없는데_로그아웃_업청() {
		LogoutCommand command = new LogoutCommand(null, null);
		Optional<JwtTokenVO> logout = logoutService.logout(command);

		assertTrue(logout.isEmpty());
	}

	@Test
	void 정당한_값으로_요청() {
		JwtTokenVO jwtTokenVO = new JwtTokenVO("testAccessToken", "testRefreshToken", "testSessionId", 0L, null);
		when(loadTokenPort.findTokenBySessionId(anyString())).thenReturn(Optional.of(jwtTokenVO));
		LogoutCommand command = new LogoutCommand("testSessionId", "testAccessToken");
		Optional<JwtTokenVO> logout = logoutService.logout(command);

		assertTrue(logout.isPresent());
	}

}
