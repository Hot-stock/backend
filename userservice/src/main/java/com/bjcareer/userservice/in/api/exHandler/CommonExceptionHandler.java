package com.bjcareer.userservice.in.api.exHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		return buildErrorResponse("입력된 정보가 잘못됐습니다", HttpStatus.BAD_REQUEST, e);
	}

	private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, Exception e) {
		log.error("Error: {}", e.getMessage());
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return new ResponseEntity<>(response, status);
	}
}
