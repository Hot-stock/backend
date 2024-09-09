package com.bjcareer.payment.adapter.out.web.psp.toss.executor;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.payment.adapter.out.web.psp.toss.exception.TossErrorCode;
import com.bjcareer.payment.adapter.out.web.psp.toss.response.TossFailureResponse;
import com.bjcareer.payment.adapter.out.web.psp.toss.response.TossPaymentExecutionResponse;
import com.bjcareer.payment.adapter.out.web.psp.PspConfirmationException;
import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@Component
@RequiredArgsConstructor
@Slf4j
public class TossPaymentExecutor implements PaymentExecutor {

	public static final int MAX_ATTEMPTS = 3;
	public static final int WAIT_TIME = 2;
	public static final double JITTER_FACTOR = 0.1;
	private final WebClient webClient;

	@Override
	public Mono<PaymentExecutionResult> execute(PaymentConfirmResult paymentConfirmResult) {
		String uri = "/v1/payments/confirm";

		log.info("Executing payment confirmation for Order ID: {}", paymentConfirmResult.getOrderId());

		Mono<TossPaymentExecutionResponse> tossPaymentExecutionResponseMono = webClient.post()
			.uri(uri)
			.header("Idempotent-Key", paymentConfirmResult.getOrderId())
			.bodyValue(paymentConfirmResult)
			.retrieve()
			.onStatus(
				httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
				clientResponse -> clientResponse.bodyToMono(TossFailureResponse.class)
					.flatMap(TossPaymentExecutor::getPspConfirmException)
			)
			.bodyToMono(TossPaymentExecutionResponse.class)
			.doOnNext(response -> log.info("Received response from Toss API for Order ID: {}", response.getOrderId()))
			.retryWhen(getRetrySpec(paymentConfirmResult));

		return tossPaymentExecutionResponseMono
			.flatMap(it -> {
				log.info("Payment confirmation successful for Order ID: {}", it.getOrderId());
				return Mono.just(new PaymentExecutionResult(it.getPaymentKey(), it.getOrderId(), it.getOrderName(),
					it.getTotalAmount(), it.getStatus(), it.getRequestedAt(), it.getApprovedAt(), true, false, false));
			});
	}

	private static Mono<Throwable> getPspConfirmException(TossFailureResponse response) {
		TossErrorCode tossErrorCode = TossErrorCode.valueOf(response.getCode());
		PspConfirmationException pspConfirmationException = new PspConfirmationException(
			HttpStatus.valueOf(response.getCode()), response.getMessage(), tossErrorCode);
		log.error("Payment confirmation failed with error code: {}, message: {}", response.getCode(), response.getMessage());
		return Mono.error(pspConfirmationException);
	}

	private RetryBackoffSpec getRetrySpec(PaymentConfirmResult paymentConfirmResult) {
		return Retry.backoff(MAX_ATTEMPTS, Duration.ofSeconds(WAIT_TIME)).jitter(JITTER_FACTOR)
			.filter(this::shouldRetry) // 특정 조건에 따라 재시도
			.doBeforeRetry(retrySignal -> log.warn("Retrying payment confirmation for Order ID: {}. Attempt: {}",
				paymentConfirmResult.getOrderId(), retrySignal.totalRetries() + 1));
	}

	private boolean shouldRetry(Throwable throwable) {
		if (throwable instanceof PspConfirmationException) {
			PspConfirmationException ex = (PspConfirmationException) throwable;
			return ex.isRetryAbleError();
		}

		if (throwable instanceof TimeoutException) {
			return true;
		}

		return false;
	}
}
