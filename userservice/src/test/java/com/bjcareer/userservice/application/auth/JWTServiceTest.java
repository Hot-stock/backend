package com.bjcareer.userservice.application.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.bjcareer.userservice.application.auth.token.JWTUtil;
import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.in.TokenRefreshCommand;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.RemoveTokenPort;
import com.bjcareer.userservice.application.ports.out.SaveTokenPort;
import com.bjcareer.userservice.domain.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

class JWTServiceTest {

	public static final String EMAIL = "test@test.com";
	public static final String PASSWORD = "password";

	@Mock
	private LoadTokenPort loadTokenPort;
	@Mock
	private SaveTokenPort saveTokenPort;
	@Mock
	private RemoveTokenPort removeTokenPort;
	private JWTService jwtService;

	private String SESSION_ID = UUID.randomUUID().toString();
	String secretKey = "WUyQS4wsRsm5tiwG49Etyj_Vp3g1kgaEAKZuXwz8sqc=";
	JWTUtil jwtUtil = new JWTUtil(secretKey);

	@BeforeEach
	void setUp() {
		loadTokenPort = mock(LoadTokenPort.class);
		saveTokenPort = mock(SaveTokenPort.class);
		removeTokenPort = mock(RemoveTokenPort.class);

		jwtService = new JWTService(jwtUtil, saveTokenPort, loadTokenPort, removeTokenPort);

	}

	@Test
	void 토큰생성_검증() {
		User user = new User(EMAIL, PASSWORD);
		JwtTokenVO result = jwtService.generateJWT(user);
		assertNotNull(result);
	}

	@Test
	void 리프래쉬토큰을_사용해서_토큰_갱신() {
		User user = new User(EMAIL, PASSWORD);
		String accessToken = generateToken(user, System.currentTimeMillis() - 2000L);
		String refreshToken = generateToken(user, System.currentTimeMillis() + 2000L);

		JwtTokenVO jwtTokenVO = new JwtTokenVO(accessToken, refreshToken, SESSION_ID, 3600000L, user.getRoles());
		TokenRefreshCommand command = new TokenRefreshCommand(SESSION_ID, refreshToken);

		when(loadTokenPort.findTokenBySessionId(SESSION_ID)).thenReturn(Optional.of(jwtTokenVO));

		// Act
		JwtTokenVO result = jwtService.generateAccessTokenViaRefresh(command);

		// Assert
		assertNotEquals(accessToken, result.getAccessToken(), "토큰을 재갱신 했음으로 값이 달라야 함");
		assertNotEquals(refreshToken, result.getRefreshToken(), "토큰을 재갱신 했음으로 값은 같아야 함");
	}

	@Test
	void 만료된_리프래쉬토큰으로_토큰_갱신_실패() {
		User user = new User(EMAIL, PASSWORD);
		String expiredRefreshToken = generateToken(user, System.currentTimeMillis() - 2000L);

		JwtTokenVO jwtTokenVO = new JwtTokenVO("dummyAccessToken", expiredRefreshToken, SESSION_ID, 3600000L, user.getRoles());
		TokenRefreshCommand command = new TokenRefreshCommand(SESSION_ID, expiredRefreshToken);

		when(loadTokenPort.findTokenBySessionId(SESSION_ID)).thenReturn(Optional.of(jwtTokenVO));

		assertThrows(UnauthorizedAccessAttemptException.class, () -> jwtService.generateAccessTokenViaRefresh(command));
	}

	@Test
	void 잘못된_리프래쉬토큰으로_토큰_갱신_실패() {
		User user = new User(EMAIL, PASSWORD);
		String invalidRefreshToken = "invalidToken";

		JwtTokenVO jwtTokenVO = new JwtTokenVO("dummyAccessToken", invalidRefreshToken, SESSION_ID, 3600000L, user.getRoles());
		TokenRefreshCommand command = new TokenRefreshCommand(SESSION_ID, invalidRefreshToken);

		when(loadTokenPort.findTokenBySessionId(SESSION_ID)).thenReturn(Optional.of(jwtTokenVO));

		assertThrows(UnauthorizedAccessAttemptException.class, () -> jwtService.generateAccessTokenViaRefresh(command));
	}


	private String generateToken(User user, long expiration) {
		Date expirationDate = new Date(expiration);
		return Jwts.builder()
			.subject(user.getEmail())
			.issuedAt(new Date())
			.expiration(expirationDate)
			.signWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey)))
			.compact();
	}
}
