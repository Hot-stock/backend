package com.bjcareer.payment.adapter.out.persistent.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;

import reactor.core.publisher.Mono;

public interface PaymentEventRepository extends ReactiveCrudRepository<PaymentEvent, Long> {
	@Query("SELECT * FROM payment_event WHERE checkout_id= :orderId")
	Mono<PaymentEvent> findByCheckoutId(String checkoutId);
}
