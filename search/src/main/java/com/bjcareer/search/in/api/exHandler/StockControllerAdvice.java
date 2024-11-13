package com.bjcareer.search.in.api.exHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bjcareer.search.application.exceptions.InvalidStockInformationException;

import lombok.Getter;

@RestControllerAdvice
public class StockControllerAdvice {

	@ExceptionHandler(InvalidStockInformationException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(InvalidStockInformationException ex) {
		return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
			HttpStatus.BAD_REQUEST);
	}

	@Getter
	static class ErrorResponse {
		private final Integer status;
		private final String message;

		public ErrorResponse(Integer status, String message) {
			this.status = status;
			this.message = message;
		}
	}
}
