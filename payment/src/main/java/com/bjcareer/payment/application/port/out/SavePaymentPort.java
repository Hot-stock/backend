package com.bjcareer.payment.application.port.out;

import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;

import reactor.core.publisher.Mono;


public interface SavePaymentPort {
	Mono<PaymentEvent> save(PaymentEvent paymentEvent);
	Mono<PaymentEvent> findById(Long id);
	Mono<Void> delete(PaymentEvent paymentEvent);
}
