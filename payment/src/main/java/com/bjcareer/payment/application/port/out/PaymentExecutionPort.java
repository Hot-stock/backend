package com.bjcareer.payment.application.port.out;

import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;
import com.bjcareer.payment.application.port.in.PaymentConfirmCommand;

import reactor.core.publisher.Mono;

public interface PaymentExecutionPort {
	Mono<PaymentExecutionResult> execute(PaymentConfirmResult paymentConfirmResult);
}
