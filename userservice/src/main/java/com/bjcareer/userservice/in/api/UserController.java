package com.bjcareer.userservice.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.userservice.application.ports.in.user.SessionUsecase;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.in.api.response.UserResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v0/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final SessionUsecase usecase;

	@GetMapping("/{sessionId}")
	public ResponseEntity<UserResponseDTO> getUser(@PathVariable("sessionId") String sessionId) {
		System.out.println("sessionId = " + sessionId);
		log.info("User request: {}", sessionId);
		User user = usecase.loadUser(sessionId);
		log.info("User found: {}", user.getEmail());
		return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getEmail()));
	}

	@GetMapping("/test")
	public String test() {
		return "test";
	}
}
