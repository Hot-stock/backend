package com.bjcareer.payment.application.port.in;

import com.bjcareer.payment.application.domain.CheckoutResult;

import reactor.core.publisher.Mono;

public interface CheckoutUsecase {
	Mono<CheckoutResult> checkout(CheckoutCommand checkoutCommand);
	Mono<CheckoutResult> validationCheckout(ValidationCheckoutCommand checkoutCommand);
}
