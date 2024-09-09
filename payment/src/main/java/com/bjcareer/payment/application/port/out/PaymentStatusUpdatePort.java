package com.bjcareer.payment.application.port.out;

import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;

import reactor.core.publisher.Mono;

public interface PaymentStatusUpdatePort {
	Mono<Boolean> updatePaymentStatus(PaymentStatusUpdateCommand command);
}
