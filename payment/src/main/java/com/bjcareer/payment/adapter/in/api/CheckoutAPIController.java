package com.bjcareer.payment.adapter.in.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.payment.adapter.in.response.ApiResponse;
import com.bjcareer.payment.application.port.in.CheckoutUsecase;
import com.bjcareer.payment.application.port.in.ValidationCheckoutCommand;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j @RequiredArgsConstructor
@RequestMapping("/api/v0/payment")
public class CheckoutAPIController {
	private final CheckoutUsecase checkoutUsecase;


	@GetMapping("/checkout/{checkoutId}")
	public Mono<ResponseEntity<ApiResponse<?>>> checkout(@PathVariable String checkoutId,  HttpServletRequest httpRequest) {
		String sessionId = httpRequest.getSession().getId();
		ValidationCheckoutCommand command = new ValidationCheckoutCommand(checkoutId, sessionId);

		return checkoutUsecase.validationCheckout(command).
			map(it -> new ResponseEntity<>(
				new ApiResponse<>(HttpStatus.OK, "OK", it),
				HttpStatus.OK
			));
	}
}
