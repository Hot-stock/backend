package com.bjcareer.payment.adapter.out.web.psp;

import org.springframework.stereotype.Component;

import com.bjcareer.payment.adapter.out.web.psp.toss.executor.PaymentExecutor;
import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;
import com.bjcareer.payment.application.port.out.PaymentExecutionPort;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PaymentExecutionWebAdapter implements PaymentExecutionPort {
	private final PaymentExecutor paymentExecutor;

	@Override
	public Mono<PaymentExecutionResult> execute(PaymentConfirmResult paymentConfirmResult) {
		return paymentExecutor.execute(paymentConfirmResult);
	}
}
