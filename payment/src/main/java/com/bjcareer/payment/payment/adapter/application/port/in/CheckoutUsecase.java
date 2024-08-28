package com.bjcareer.payment.payment.adapter.application.port.in;

import com.bjcareer.payment.payment.adapter.application.port.domain.CheckoutResult;

import reactor.core.publisher.Mono;

public interface CheckoutUsecase {
	Mono<CheckoutResult> checkout(CheckoutCommand checkoutCommand);
}
