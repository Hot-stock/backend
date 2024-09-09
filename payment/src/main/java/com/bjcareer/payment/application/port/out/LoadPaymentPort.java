package com.bjcareer.payment.application.port.out;

import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import reactor.core.publisher.Mono;

public interface LoadPaymentPort {
	Mono<PaymentEvent> getPaymentByCheckoutId(String checkoutId);
}
