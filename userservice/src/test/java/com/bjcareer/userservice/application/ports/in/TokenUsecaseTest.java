package com.bjcareer.userservice.application.ports.in;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.domain.entity.User;

@SpringBootTest
class TokenUsecaseTest {
	String MAIL = "test@test.com";
	String PASSWORD = "password";

	@Autowired TokenUsecase tokenUsecase;
	@Autowired
	LoadTokenPort loadTokenPort;

	@Test
	void 인가된_사용자에게_적합한_jwt를_생성하는지(){
		User user = new User(MAIL, PASSWORD);
		JwtTokenVO jwtTokenVO = tokenUsecase.generateJWT(user);
		assertNotNull(jwtTokenVO, "발급된 JWT가 없음");

		Optional<JwtTokenVO> tokenBySessionId = loadTokenPort.findTokenBySessionId(jwtTokenVO.getSessionId());
		assertFalse(tokenBySessionId.isEmpty(), "세션 아이디로 토큰을 찾을 수 없음");
		JwtTokenVO target = tokenBySessionId.get();

		assertEquals(jwtTokenVO.getAccessToken(), target.getAccessToken(), "발급된 토큰이 일치하지 않음");
		assertEquals(jwtTokenVO.getRefreshToken(), target.getRefreshToken(), "발급된 토큰이 일치하지 않음");
		assertEquals(jwtTokenVO.getSessionId(), target.getSessionId(), "발급된 토큰이 일치하지 않음");;
	}

}
