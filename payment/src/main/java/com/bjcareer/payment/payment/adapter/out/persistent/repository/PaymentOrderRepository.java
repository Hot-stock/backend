package com.bjcareer.payment.payment.adapter.out.persistent.repository;

import java.util.Queue;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.bjcareer.payment.payment.adapter.application.port.domain.entity.order.PaymentOrder;

import reactor.core.publisher.Flux;

public interface PaymentOrderRepository extends ReactiveCrudRepository<PaymentOrder, Long> {

	@Query("SELECT * FROM payment_order WHERE payment_event_id = :paymentEventId")
	Flux<PaymentOrder> findByPaymentEventId(Long paymentEventId);
}
