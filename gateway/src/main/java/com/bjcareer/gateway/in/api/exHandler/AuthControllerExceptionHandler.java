package com.bjcareer.gateway.in.api.exHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bjcareer.gateway.exceptions.UnauthorizedAccessAttemptException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AuthControllerExceptionHandler {
	@ExceptionHandler(UnauthorizedAccessAttemptException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorizedAccessAttemptException(
		UnauthorizedAccessAttemptException e) {
		return buildErrorResponse("Unauthorized access attempt detected.", HttpStatus.UNAUTHORIZED, e);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> buildErrorResponse(Exception e) {
		return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
	}

	private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, Exception e) {
		log.error("Error: {}", e.getMessage());
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, status);
	}
}
