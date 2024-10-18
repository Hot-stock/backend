package com.bjcareer.search.in.api.exHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bjcareer.search.in.api.controller.StockController;
import com.bjcareer.search.application.exceptions.InvalidStockInformation;

import lombok.Getter;

@RestControllerAdvice(assignableTypes = {StockController.class})
public class StockControllerAdvice {

	@ExceptionHandler(InvalidStockInformation.class)
	public ResponseEntity<Object> handleIllegalArgumentException(InvalidStockInformation ex) {
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
