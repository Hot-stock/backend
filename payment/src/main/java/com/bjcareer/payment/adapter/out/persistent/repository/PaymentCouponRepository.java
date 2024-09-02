package com.bjcareer.payment.adapter.out.persistent.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;

import reactor.core.publisher.Flux;

public interface PaymentCouponRepository extends ReactiveCrudRepository<PaymentCoupon, Long> {

	@Query("SELECT * FROM payment_coupon WHERE payment_event_id = :paymentEventId")
	Flux<PaymentCoupon> findByPaymentEventId(Long paymentEventId);
}
