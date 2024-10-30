package com.bjcareer.userservice.FuncetionalTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.ports.out.CreateUserPort;
import com.bjcareer.userservice.common.CookieHelper;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.in.api.AuthController;
import com.bjcareer.userservice.in.api.request.LoginRequestDTO;
import com.bjcareer.userservice.in.api.response.LoginResponseDTO;

import jakarta.servlet.http.Cookie;

@SpringBootTest
class AuthControllerIntegrationTest {
	private static final Logger log = LoggerFactory.getLogger(AuthControllerIntegrationTest.class);
	@Autowired
	AuthController authController;

	@Autowired
	CreateUserPort userPort;

	@Test
	@Transactional(readOnly = true)
	void 로그인실패_회원가입안함() {
		String email = "test@test.com";
		String password = "1234";

		LoginRequestDTO requestDTO = new LoginRequestDTO(email, password);
		MockHttpServletResponse response = new MockHttpServletResponse();

		assertThrows(UnauthorizedAccessAttemptException.class, () -> authController.Login(requestDTO, response));
	}

	@Test
	@Transactional
	void 로그인실패_패스워드_불일치() {
		String email = "test12@asd.com";
		String password = "test";
		String wrongPassword = "test1";

		LoginRequestDTO requestDTO = new LoginRequestDTO(email, wrongPassword);
		MockHttpServletResponse response = new MockHttpServletResponse();

		User user = new User(email, password);

		userPort.save(user);
		assertThrows(UnauthorizedAccessAttemptException.class, () -> authController.Login(requestDTO, response));
	}

	@Test
	@Transactional
	void 로그인_성공() {
		String email = "test12@asd.com";
		String password = "test";

		User user = new User(email, password);
		userPort.save(user);

		LoginRequestDTO requestDTO = new LoginRequestDTO(email, password);
		MockHttpServletResponse response = new MockHttpServletResponse();

		ResponseEntity<LoginResponseDTO> login = authController.Login(requestDTO, response);
		assertEquals(HttpStatus.OK, login.getStatusCode());
	}

	@Test
	@Transactional
	void 잘못된_세션아이디로_로그아웃_요청() {
		String email = "test12@asd.com";
		String password = "test";

		User user = new User(email, password);
		userPort.save(user);

		ResponseEntity<?> logout = authController.Logout("wrongSessionId", "accessToken",
			new MockHttpServletResponse());

		assertEquals(HttpStatus.BAD_REQUEST, logout.getStatusCode());
	}

	@Test
	@Transactional
	void 정당한_로그아웃_요청() {
		String email = "test12@asd.com";
		String password = "test";

		User user = new User(email, password);
		userPort.save(user);

		MockHttpServletResponse response = new MockHttpServletResponse();
		ResponseEntity<LoginResponseDTO> login = authController.Login(new LoginRequestDTO(email, password),
			response);

		assertEquals(HttpStatus.OK, login.getStatusCode());

		Cookie sessionCookies = response.getCookie(CookieHelper.SESSION_ID);
		Cookie accessToken = response.getCookie(CookieHelper.ACCESS_TOKEN);

		ResponseEntity<HttpStatus> logout = authController.Logout(sessionCookies.getValue(), accessToken.getValue(),
			response);

		assertEquals(HttpStatus.OK, logout.getStatusCode());
	}

	@Test
	@Transactional
	void 리프래쉬토큰_드래프트_일때_요청() {
		String email = "test12@asd.com";
		String password = "test";

		User user = new User(email, password);
		userPort.save(user);

		MockHttpServletResponse response = new MockHttpServletResponse();
		ResponseEntity<LoginResponseDTO> login = authController.Login(new LoginRequestDTO(email, password),
			response);

		assertEquals(HttpStatus.OK, login.getStatusCode());

		assertThrows(UnauthorizedAccessAttemptException.class, () -> authController.refreshLogin(
			response.getCookie(CookieHelper.SESSION_ID).getValue(),
			response.getCookie(CookieHelper.REFRESH_TOKEN).getValue(), response));
	}

}
