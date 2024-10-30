package com.bjcareer.userservice.FuncetionalTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bjcareer.userservice.application.exceptions.VerifyTokenDoesNotExist;
import com.bjcareer.userservice.in.api.RegisterController;
import com.bjcareer.userservice.in.api.request.MobileAuthenticationVerifyRequestDTO;
import com.bjcareer.userservice.in.api.request.RegisterRequestDTO;
import com.bjcareer.userservice.in.api.request.VerifyEmailRequestDTO;
import com.bjcareer.userservice.in.api.response.MobileAuthenticationVerifyResponseDTO;

@SpringBootTest
class RegisterControllerIntegrationTest {
	@Autowired
	RegisterController registerController;

	@Test
	void 회원가입토큰발송() {
		VerifyEmailRequestDTO request = new VerifyEmailRequestDTO("wodhksqw@naver.com");
		registerController.generateTokenForRegister(request);
	}

	@Test
	void 미인증일떄_회원가입_요청() {
		RegisterRequestDTO request = new RegisterRequestDTO("email", "password");
		assertThrows(VerifyTokenDoesNotExist.class, () -> registerController.register(request));
	}

	@Test
	void 틀립번호로_인증_요청() {
		MobileAuthenticationVerifyRequestDTO request = new MobileAuthenticationVerifyRequestDTO("email", 1L);
		ResponseEntity<MobileAuthenticationVerifyResponseDTO> res = registerController.verifyEmailToken(
			request);

		assertEquals(false, res.getBody().isResult());
		assertEquals(HttpStatus.OK, res.getStatusCode());
	}
}
