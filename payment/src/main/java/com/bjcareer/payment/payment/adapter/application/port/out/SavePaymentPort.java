package com.bjcareer.payment.payment.adapter.application.port.out;

import com.bjcareer.payment.payment.adapter.application.port.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.payment.adapter.application.port.domain.entity.order.PaymentOrder;

import reactor.core.publisher.Mono;


public interface SavePaymentPort {
	Mono<PaymentEvent> save(PaymentEvent paymentEvent);
	Mono<PaymentEvent> findById(Long id);
	Mono<Void> delete(PaymentEvent paymentEvent);
}
