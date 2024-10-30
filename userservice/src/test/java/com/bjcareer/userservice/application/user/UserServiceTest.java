package com.bjcareer.userservice.application.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bjcareer.userservice.application.auth.token.JWTUtil;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.LoadUserPort;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.out.persistance.repository.exceptions.UserNotFoundException;

import io.jsonwebtoken.Claims;

class UserServiceTest {

	@Mock
	private LoadUserPort loadUserPort;
	@Mock
	private LoadTokenPort loadTokenPort;
	@Mock
	private JWTUtil jwtUtil;

	@Mock
	private Claims claims;

	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		userService = new UserService(loadTokenPort, loadUserPort, jwtUtil);
	}

	@Test
	void 찾아진_세션_아이디가_없을경우() {
		String sessionId = "InvalidSessionId";
		when(loadTokenPort.findTokenBySessionId(sessionId)).thenReturn(Optional.empty());
		assertThrows(UnauthorizedAccessAttemptException.class, () -> {
			userService.loadUser(sessionId);
		});
	}

	@Test
	void 찾아진_세션_아이디가_있지만_이메일이_디비로_안찾아지는_경우() {
		String sessionId = UUID.randomUUID().toString();
		String accessToken = UUID.randomUUID().toString();
		String refreshToken = UUID.randomUUID().toString();
		Long refreshTokenExp = 1000L;

		List<RoleType> roleTypes = new ArrayList<>();

		roleTypes.add(RoleType.USER);
		roleTypes.add(RoleType.ADMIN);

		when(loadTokenPort.findTokenBySessionId(sessionId)).thenReturn(
			Optional.of(new JwtTokenVO(accessToken, refreshToken, sessionId, refreshTokenExp, roleTypes)));
		when(jwtUtil.parseToken(accessToken)).thenReturn(claims);
		when(loadUserPort.findByEmail(anyString())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> {
			userService.loadUser(sessionId);
		});
	}
}
