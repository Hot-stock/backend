package com.bjcareer.payment.payment.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.payment.payment.adapter.application.port.domain.CheckoutResult;
import com.bjcareer.payment.payment.adapter.application.port.in.CheckoutCommand;
import com.bjcareer.payment.payment.adapter.application.port.in.CheckoutUsecase;
import com.bjcareer.payment.payment.adapter.in.request.CheckoutRequest;
import com.bjcareer.payment.payment.adapter.in.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0")
public class CheckoutController {
	private final CheckoutUsecase checkoutUsecase;

	@PostMapping("/checkout")
	public Mono<ResponseEntity<ApiResponse>> checkout(@RequestBody CheckoutRequest checkoutRequest){
		System.out.println("checkoutRequest = " + checkoutRequest);
		CheckoutCommand checkoutCommand = new CheckoutCommand(checkoutRequest.getCartId(), checkoutRequest.getBuyerPk(),
			checkoutRequest.getProductIds(), checkoutRequest.getCouponIds());

		Mono<CheckoutResult> checkout = checkoutUsecase.checkout(checkoutCommand);
		return checkout.map(response -> ResponseEntity.ok(new ApiResponse(HttpStatus.CREATED, "OK", response)));
	}
}
