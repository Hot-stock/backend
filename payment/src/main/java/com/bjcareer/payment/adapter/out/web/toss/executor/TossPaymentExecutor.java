package com.bjcareer.payment.adapter.out.web.toss.executor;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.payment.adapter.out.web.toss.exception.PspConfirmationException;
import com.bjcareer.payment.adapter.out.web.toss.exception.TossErrorCode;
import com.bjcareer.payment.adapter.out.web.toss.response.TossFailureResponse;
import com.bjcareer.payment.adapter.out.web.toss.response.TossPaymentExecutionResponse;
import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;


import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
public class TossPaymentExecutor implements PaymentExecutor {
	private final WebClient webClient;


	@Override
	public Mono<PaymentExecutionResult> execute(PaymentConfirmResult paymentConfirmResult) {
		String uri = "/v1/payments/confirm";
		Mono<TossPaymentExecutionResponse> tossPaymentExecutionResponseMono = webClient.post()
			.uri(uri)
			.header("Idempotent-Key", paymentConfirmResult.getOrderId())
			.bodyValue(paymentConfirmResult)
			.retrieve()
			.onStatus(
				httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
				clientResponse -> clientResponse.bodyToMono(TossFailureResponse.class)
					.flatMap(it -> {
						TossErrorCode tossErrorCode = TossErrorCode.valueOf(it.getCode());
						PspConfirmationException pspConfirmationException = new PspConfirmationException(
							it.getCode(), it.getMessage(), tossErrorCode);
						return Mono.error(pspConfirmationException);
					})
			)
			.bodyToMono(TossPaymentExecutionResponse.class)
			.retryWhen(Retry.backoff(3, Duration.ofSeconds(2)).jitter(2.0));

		return tossPaymentExecutionResponseMono.flatMap(it -> Mono.just(new PaymentExecutionResult(it.getPaymentKey(), it.getOrderId(), it.getOrderName(), it.getTotalAmount(), it.getStatus(), it.getRequestedAt(), it.getApprovedAt(), true, false, false)));
	}

}
