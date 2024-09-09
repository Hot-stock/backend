package com.bjcareer.payment.adapter.out.persistent.r2dbc;

import org.springframework.stereotype.Repository;

import com.bjcareer.payment.adapter.out.persistent.repository.PaymentValidationRepository;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.adapter.out.persistent.repository.PaymentEventRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class R2DBCPaymentValidationRepository implements PaymentValidationRepository {
	private final PaymentEventRepository paymentEventRepository;

	@Override
	public Mono<Boolean> isValid(String checkoutId, Long amount) {
		Mono<PaymentEvent> byOrderId = paymentEventRepository.findByCheckoutId(checkoutId);
		return byOrderId.flatMap(it -> Mono.just(it.getTotalAmount().equals(amount)));
	}
}
