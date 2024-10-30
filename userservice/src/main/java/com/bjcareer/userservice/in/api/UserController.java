package com.bjcareer.userservice.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.userservice.application.ports.in.user.SessionUsecase;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.in.api.response.UserResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v0/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final SessionUsecase usecase;

	@GetMapping("/{sessionId}")
	@Operation(summary = "세션 아이디를 통해서 유저를 찾는 기능", description = "백엔드에서 User를 검증하기 위해서 사용된다", responses = {
		@ApiResponse(responseCode = "200", description = "찾기 성공"),
		@ApiResponse(responseCode = "400", description = "찾기 실패")
	})
	public ResponseEntity<UserResponseDTO> getUserInSessionId(@PathVariable("sessionId") String sessionId) {
		log.debug("User request: {}", sessionId);
		User user = usecase.loadUser(sessionId);
		log.debug("User found: {}", user.getEmail());
		return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getEmail()));
	}

	@GetMapping("/test")
	public String test() {
		return "test";
	}
}
