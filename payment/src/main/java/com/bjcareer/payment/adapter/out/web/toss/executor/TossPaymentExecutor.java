package com.bjcareer.payment.adapter.out.web.toss.executor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.payment.adapter.out.web.toss.response.TossPaymentExecutionResponse;
import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;


import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

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
				clientResponse -> Mono.error(new RuntimeException("Failed to communicate with Toss: " + clientResponse.statusCode()))
			)
			.bodyToMono(TossPaymentExecutionResponse.class);

		return tossPaymentExecutionResponseMono.flatMap(it -> Mono.just(new PaymentExecutionResult(it.getPaymentKey(), it.getOrderId(), it.getOrderName(), it.getTotalAmount(), it.getStatus(), it.getRequestedAt(), it.getApprovedAt(), true, false, false)));
	}
}
