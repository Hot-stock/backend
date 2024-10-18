package com.bjcareer.userservice.in.api.exHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bjcareer.userservice.application.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.out.persistance.repository.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserExceptionHandler {

	@ExceptionHandler(UnauthorizedAccessAttemptException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorizedAccessAttemptException(
		UnauthorizedAccessAttemptException e) {
		return buildErrorResponse("SessionID Does not exist.", HttpStatus.BAD_REQUEST, e);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException e) {
		return buildErrorResponse("Unauthorized access attempt detected.", HttpStatus.NOT_FOUND, e);
	}

	private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, Exception e) {
		log.error("Error: {}", e.getMessage());
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, status);
	}
}
