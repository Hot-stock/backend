package com.bjcareer.payment.adapter.out.persistent.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrderHistory;

import reactor.core.publisher.Mono;

public interface PaymentOrderHistoryRepository extends ReactiveCrudRepository<PaymentOrderHistory, Long> {
	@Query("SELECT * FROM payment_order_history WHERE payment_order_id= :paymentOrderId")
	Mono<PaymentOrderHistory> findByOrderId(Long paymentOrderId);
}
