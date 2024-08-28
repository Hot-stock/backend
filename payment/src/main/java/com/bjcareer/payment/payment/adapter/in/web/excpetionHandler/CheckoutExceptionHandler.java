package com.bjcareer.payment.payment.adapter.in.web.excpetionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bjcareer.payment.payment.adapter.application.service.excpetions.CheckoutFailedException;
import com.bjcareer.payment.payment.adapter.application.service.excpetions.DuplicatedCheckout;
import com.bjcareer.payment.payment.adapter.in.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CheckoutExceptionHandler {

	@ExceptionHandler(CheckoutFailedException.class)
	public ResponseEntity<ApiResponse> handleCheckoutException(CheckoutFailedException e) {
		log.error("Checkout failed: {}", e.getMessage());
		return buildResponseEntity(
			HttpStatus.INTERNAL_SERVER_ERROR,
			"서버가 혼잡합니다. 잠시 후 이용해주세요"
		);
	}

	@ExceptionHandler(DuplicatedCheckout.class)
	public ResponseEntity<ApiResponse> handleDuplicatedCheckoutException(DuplicatedCheckout e) {
		log.error("Duplicated checkout attempt: {}", e.getMessage());
		return buildResponseEntity(
			HttpStatus.TOO_MANY_REQUESTS,
			"진행중인 결제건이 있습니다."
		);
	}

	private ResponseEntity<ApiResponse> buildResponseEntity(HttpStatus status, String message) {
		ApiResponse response = new ApiResponse(status, message, null);
		return ResponseEntity.status(status).body(response);
	}
}
