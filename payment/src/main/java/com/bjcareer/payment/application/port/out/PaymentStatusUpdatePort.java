package com.bjcareer.payment.application.port.out;

import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;

import reactor.core.publisher.Mono;

public interface PaymentStatusUpdatePort {
	Mono<Void> updatePaymentStatusToExecuting(String orderId, String eventId);

	Mono<Boolean> updatePaymentStatus(PaymentStatusUpdateCommand command);
}
