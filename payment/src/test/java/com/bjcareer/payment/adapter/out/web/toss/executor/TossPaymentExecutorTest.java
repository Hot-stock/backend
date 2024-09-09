package com.bjcareer.payment.adapter.out.web.toss.executor;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.payment.adapter.out.web.toss.exception.PspConfirmationException;
import com.bjcareer.payment.adapter.out.web.toss.exception.TossErrorCode;
import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;
import com.bjcareer.payment.helper.PSPWebClientConfiguration;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@Import(PSPWebClientConfiguration.class)
class TossPaymentExecutorTest {
	public static final long AMOUNT = 2000L;

	@Autowired
	private PSPWebClientConfiguration pspWebClientConfiguration;

	@Test
	void whenNonRetryableErrorIsReceived() {
		WebClient webClient = pspWebClientConfiguration.tossPaymentWebClient(TossErrorCode.EXCEED_MAX_AMOUNT.toString());
		TossPaymentExecutor tossPaymentExecutor = new TossPaymentExecutor(webClient);
		Mono<PaymentExecutionResult> execute = tossPaymentExecutor.execute(
			new PaymentConfirmResult("TEST_KEY", "TEST_ID", AMOUNT));

		StepVerifier.create(execute)
			.expectErrorMatches(throwable -> throwable instanceof PspConfirmationException)
			.verify();
	}

	@Test
	void whenRetryableErrorIsReceived() {
		WebClient webClient = pspWebClientConfiguration.tossPaymentWebClient(TossErrorCode.ACCOUNT_OWNER_CHECK_FAILED.toString());
		TossPaymentExecutor tossPaymentExecutor = new TossPaymentExecutor(webClient);
		Mono<PaymentExecutionResult> execute = tossPaymentExecutor.execute(
			new PaymentConfirmResult("TEST_KEY", "TEST_ID", AMOUNT));

		StepVerifier.create(execute)
			.expectErrorMatches(throwable -> throwable instanceof IllegalStateException)
			.verify();
	}

}
