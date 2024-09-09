package com.bjcareer.payment.adapter.out.web.toss.executor;

import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;
import com.bjcareer.payment.application.port.in.PaymentConfirmCommand;

import reactor.core.publisher.Mono;

public interface PaymentExecutor {
	Mono<PaymentExecutionResult> execute(PaymentConfirmResult paymentConfirmResult);
}
