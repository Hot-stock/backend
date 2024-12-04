package com.bjcareer.gateway.in.api.exHandler;

import java.nio.channels.ClosedChannelException;

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
		log.error("API LIMIT: {}", e.getMessage());
		return buildErrorResponse(e.getMessage(), HttpStatus.TOO_MANY_REQUESTS, e);
	}

	@ExceptionHandler(ClosedChannelException.class)
	public ResponseEntity<ResponseDomain<?>> closedChannelExceptionHandler(ClosedChannelException e) {
		log.error("API LIMIT: {}", e.getMessage());
		return buildErrorResponse("서비스의 이용자가 많아 일시적으로 서비스를 이용할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE, e);
	}



	public static ResponseEntity<ResponseDomain<?>> buildErrorResponse(String message, HttpStatus status, Exception e) {
		ResponseDomain<Object> objectResponseDomain = new ResponseDomain<>(status, null, new ErrorDomain(message));
		return new ResponseEntity<>(objectResponseDomain, status);
	}
}
