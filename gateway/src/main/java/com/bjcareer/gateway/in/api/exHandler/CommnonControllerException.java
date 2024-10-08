package com.bjcareer.gateway.in.api.exHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bjcareer.gateway.domain.ErrorDomain;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.exceptions.TooManyRequestsException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CommnonControllerException {
	@ExceptionHandler(TooManyRequestsException.class)
	public ResponseEntity<ResponseDomain<?>> apiLimitErrorResponse(TooManyRequestsException e) {
		return buildErrorResponse(e.getMessage(), HttpStatus.TOO_MANY_REQUESTS, e);
	}

	public static ResponseEntity<ResponseDomain<?>> buildErrorResponse(String message, HttpStatus status, Exception e) {
		log.error("Error: {}", e.getMessage());
		ResponseDomain<Object> objectResponseDomain = new ResponseDomain<>(status, null, new ErrorDomain(message));
		return new ResponseEntity<>(objectResponseDomain, status);
	}
}
