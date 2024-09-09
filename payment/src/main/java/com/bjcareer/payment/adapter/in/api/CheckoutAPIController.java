package com.bjcareer.payment.adapter.in.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.payment.adapter.in.request.VaildationCheckoutRequest;
import com.bjcareer.payment.adapter.in.response.ApiResponse;
import com.bjcareer.payment.application.port.in.CheckoutUsecase;
import com.bjcareer.payment.application.port.in.ValidationCheckoutCommand;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j @RequiredArgsConstructor
@RequestMapping("/api/v0/payment")
public class CheckoutAPIController {
	private final CheckoutUsecase checkoutUsecase;

	@PostMapping("/checkout/{checkoutId}")
	public Mono<ResponseEntity<ApiResponse<?>>> checkout(@PathVariable String checkoutId, @RequestBody VaildationCheckoutRequest request) {
		ValidationCheckoutCommand command = new ValidationCheckoutCommand(request.getBuyerId(), checkoutId);
		log.debug("command is {}", command);

		return checkoutUsecase.validationCheckout(command)
			.doOnNext(it -> log.debug("Validation result: {}", it))
			.map(it -> new ResponseEntity<>(
				new ApiResponse<>(HttpStatus.OK, "OK", it), HttpStatus.OK));
	}

}
