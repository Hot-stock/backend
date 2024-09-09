package com.bjcareer.payment.application.port.out;

import reactor.core.publisher.Mono;

public interface PaymentValidationPort {
	Mono<Boolean> isVaild(String orderId, Long totalAmount);
}
