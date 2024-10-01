package com.bjcareer.userservice.application.register;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.userservice.application.ports.in.RegisterRequestCommand;
import com.bjcareer.userservice.application.ports.in.RegisterUsecase;
import com.bjcareer.userservice.commonTest.UsecaseTest;

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
	void 정상적인_회원가입_flow() {
		String email = "wodhksqw@naver.com";

		//인증 요청
		Long verifyToken = registerUsecase.generateRandomTokenForAuthentication(email);
		//인증 성공
		boolean result = registerUsecase.verifyToken(email, verifyToken);
		assertTrue(result);

		//회원가입
		Long id = registerUsecase.registerService(new RegisterRequestCommand(email, "test"));
		assertNotNull(id);
	}

	@Test
	void 비정상적인_회원가입시도_중_토큰번호_인증_실패() {
		String email = "wodhksqw@naver.com";
		Long worngVerifyToken = 123456L;

		registerUsecase.generateRandomTokenForAuthentication(email);
		boolean verifieResult = registerUsecase.verifyToken(email, worngVerifyToken);
		assertFalse(verifieResult);

		assertThrows(VerifyTokenDoesNotExist.class,
			() -> registerUsecase.registerService(new RegisterRequestCommand(email, "test")));
	}

	@Test
	void 비정상적인_회원가입_시도_인증_없이_요청() {
		String email = "wodhksqw@naver.com";
		String password = "test";
		RegisterRequestCommand command = new RegisterRequestCommand(email, password);

		assertThrows(VerifyTokenDoesNotExist.class, () -> registerUsecase.registerService(command));
	}

	@Test
	void 비정상적인_회원가입시도_모든_인증은_통과_회원가입시_다른_ID로_요청() {
		String email = "wodhksqw@naver.com";
		String attackEmail = "wodhksqw1@naver.com";

		//인증 요청
		Long verifyToken = registerUsecase.generateRandomTokenForAuthentication(email);
		//인증 성공
		registerUsecase.verifyToken(email, verifyToken);

		//회원가입
		assertThrows(VerifyTokenDoesNotExist.class,
			() -> registerUsecase.registerService(new RegisterRequestCommand(attackEmail, "test")));
	}
}
