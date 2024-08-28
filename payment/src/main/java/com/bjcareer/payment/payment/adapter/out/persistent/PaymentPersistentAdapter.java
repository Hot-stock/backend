package com.bjcareer.payment.payment.adapter.out.persistent;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import com.bjcareer.payment.payment.adapter.application.port.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.payment.adapter.application.port.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.payment.adapter.application.port.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.payment.adapter.application.port.out.SavePaymentPort;
import com.bjcareer.payment.payment.adapter.out.persistent.repository.PaymentCouponRepository;
import com.bjcareer.payment.payment.adapter.out.persistent.repository.PaymentEventRepository;
import com.bjcareer.payment.payment.adapter.out.persistent.repository.PaymentOrderRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PaymentPersistentAdapter implements SavePaymentPort {

	private final PaymentEventRepository paymentRepository;
	private final PaymentOrderRepository paymentOrderRepository;
	private final PaymentCouponRepository paymentCouponRepository;
	private final TransactionalOperator transactionalOperator;
	private final PaymentEventRepository paymentEventRepository;

	@Override
	public Mono<PaymentEvent> save(PaymentEvent paymentEvent) {
		return transactionalOperator.transactional(
			paymentRepository.save(paymentEvent)
				.flatMap(savedPayment ->
					saveOrders(savedPayment)
						.then(saveCoupons(savedPayment))
						.thenReturn(savedPayment)
				)
		);
	}

	@Override
	public Mono<PaymentEvent> findById(Long id) {
		return paymentRepository.findById(id)
			.flatMap(it ->
				Mono.zip(
					paymentOrderRepository.findByPaymentEventId(it.getId()).collectList(),
					paymentCouponRepository.findByPaymentEventId(it.getId()).collectList()
				).flatMap(tuple -> {
					List<PaymentOrder> t1 = tuple.getT1();
					List<PaymentCoupon> t2 = tuple.getT2();
					PaymentEvent paymentEvent = new PaymentEvent(it.getId(), it.getBuyerId(), it.getOrderId(), t1, t2);
					return Mono.just(paymentEvent);
				})
			);
	}

	@Override
	public Mono<Void> delete(PaymentEvent paymentEvent) {
		return paymentRepository.delete(paymentEvent);
	}

	private Mono<Void> saveOrders(PaymentEvent paymentEvent) {
		return Flux.fromIterable(paymentEvent.getOrders())
			.flatMap(order -> {
				order.assignRelatedPaymentEventId(paymentEvent.getId());
				return paymentOrderRepository.save(order);
			})
			.then();
	}

	private Mono<Void> saveCoupons(PaymentEvent paymentEvent) {
		return Flux.fromIterable(paymentEvent.getCoupons())
			.flatMap(coupon -> {
				coupon.assignRelatedPaymentEventId(paymentEvent.getId());
				return paymentCouponRepository.save(coupon);
			})
			.then();
	}

}
