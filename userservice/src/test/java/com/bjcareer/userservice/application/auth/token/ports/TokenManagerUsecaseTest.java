// package com.bjcareer.userservice.application.auth.token.ports;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import java.util.List;
// import java.util.UUID;
//
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
// import com.bjcareer.userservice.commonTest.UsecaseTest;
// import com.bjcareer.userservice.domain.entity.RoleType;
//
// import io.jsonwebtoken.Claims;
//
// @UsecaseTest
// @SpringBootTest
// class TokenManagerUsecaseTest {
// 	@Autowired
// 	TonkenManagerUsecase tokenManagerUsecase;
// 	String email = "test@test.com";
//
// 	@Test
// 	void testGenerateToken() {
// 		String sessionId = UUID.randomUUID().toString();
// 		List<RoleType> roles = List.of(RoleType.USER);
// 		JwtTokenVO jwtTokenVO = tokenManagerUsecase.generateToken(email, sessionId, roles);
// 		assertNotNull(jwtTokenVO);
// 	}
//
// 	@Test
// 	void verifyToken() {
// 		String sessionId = UUID.randomUUID().toString();
// 		List<RoleType> roles = List.of(RoleType.USER);
// 		JwtTokenVO jwtTokenVO = tokenManagerUsecase.generateToken(email, sessionId, roles);
//
// 		Claims claims = tokenManagerUsecase.verifyToken(jwtTokenVO.getAccessToken());
// 		assertNotNull(claims);
// 	}
// }
