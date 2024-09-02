package com.bjcareer.payment.adapter.out.web.toss.executor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@SpringBootTest
class TossPaymentExecutorTest {
	@Autowired PaymentExecutor paymentExecutor;

	@Test
	void 토스_요청가는_지테스트(){
		PaymentConfirmResult paymentConfirmResult = new PaymentConfirmResult("PAYMENT_KEY", "ORDER_ID", 1L);
		Mono<PaymentExecutionResult> execute = paymentExecutor.execute(paymentConfirmResult);

		StepVerifier.create(execute)
		.expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
			throwable.getMessage().equals("Failed to communicate with Toss: 404 NOT_FOUND"))
			.verify();
	}

}