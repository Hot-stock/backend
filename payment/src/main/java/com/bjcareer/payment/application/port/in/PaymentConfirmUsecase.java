package com.bjcareer.payment.application.port.in;

import com.bjcareer.payment.application.domain.PaymentExecutionResult;

import reactor.core.publisher.Mono;

public interface PaymentConfirmUsecase {
	Mono<PaymentExecutionResult> confirm(PaymentConfirmCommand command);
}
