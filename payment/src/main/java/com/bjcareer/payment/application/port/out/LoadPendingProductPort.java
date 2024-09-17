package com.bjcareer.payment.application.port.out;

import com.bjcareer.payment.application.domain.entity.event.PendingPaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoadPendingProductPort {
	Flux<PendingPaymentEvent> loadPendingProduct(PaymentStatus status);
	Mono<Long> loadAmountOfPendingPayment(Long paymentEventId);
}
