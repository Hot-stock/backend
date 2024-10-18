package com.bjcareer.userservice.application.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.userservice.application.auth.token.JWTUtil;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.in.user.SessionUsecase;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.LoadUserPort;
import com.bjcareer.userservice.domain.entity.User;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements SessionUsecase {
	private final LoadTokenPort loadTokenUsecase;
	private final LoadUserPort loadUserPort;
	private final JWTUtil jwtUtil;

	@Override
	public User loadUser(String sessionId) {
		Optional<JwtTokenVO> tokenBySessionId = loadTokenUsecase.findTokenBySessionId(sessionId);

		if (tokenBySessionId.isEmpty()) {
			log.error("Invalid session id {}", sessionId);
			throw new RuntimeException("Invalid session");
		}

		JwtTokenVO jwtTokenVO = tokenBySessionId.get();
		Claims claims = jwtUtil.parseToken(jwtTokenVO.getAccessToken());
		String userEmail = claims.getSubject();

		Optional<User> optUser = loadUserPort.findByEmail(userEmail);

		if (optUser.isEmpty()){
			log.error("Invalid email {}", userEmail);
			throw new RuntimeException("Invalid session");
		}

		return optUser.get();
	}
}
