package com.bjcareer.gateway.in.api.exHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.gateway.in.api.AuthController;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice(assignableTypes = {AuthController.class})
@RequiredArgsConstructor
public class AuthControllerExceptionHandler {
	private final Logger log;

	@ExceptionHandler(UnauthorizedAccessAttemptException.class)
	public ResponseEntity<ResponseDomain<?>> handleUnauthorizedAccessAttemptException(
		UnauthorizedAccessAttemptException e) {
		log.error("Error {} {}", e.getMessage(), "Unauthorized access attempt detected");
		return CommnonControllerException.buildErrorResponse(
			"Unauthorized access attempt detected", HttpStatus.UNAUTHORIZED, e);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDomain<?>> buildErrorResponse(Exception e) {
		return CommnonControllerException.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
	}
}
