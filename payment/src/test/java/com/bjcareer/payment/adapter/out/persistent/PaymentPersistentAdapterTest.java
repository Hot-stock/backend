package com.bjcareer.payment.adapter.out.persistent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.payment.adapter.out.persistent.repository.PaymentEventRepository;
import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.adapter.out.persistent.PaymentPersistentAdapter;
import com.bjcareer.payment.helper.HelperPayment;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class PaymentPersistentAdapterTest {

	@Autowired
	PaymentPersistentAdapter paymentPersistentAdapter;
	PaymentEvent paymentEvent;

	@BeforeEach
	void setUp() {
		paymentEvent = HelperPayment.createPayment();
		Mono<PaymentEvent> save = paymentPersistentAdapter.save(paymentEvent);
		paymentEvent = save.block();
	}

	@AfterEach
	void tearDown(){
		Mono<Void> delete = paymentPersistentAdapter.delete(paymentEvent);
		delete.block();
	}

	@Test
	void payment객체_저장() {
		//when
		PaymentEvent target = HelperPayment.createPayment();

		//act
		Mono<PaymentEvent> save = paymentPersistentAdapter.save(target);
		PaymentEvent savedPayment = save.block();

		//then
		assertNotNull(savedPayment.getId());
		assertEquals(1, savedPayment.getOrders().size());
		assertEquals(1, savedPayment.getCoupons().size());

		paymentPersistentAdapter.delete(target).block();
	}


	@Test
	void Payment_이벤트_객체를_findId로_통해서_모두_복원하는지() {
		Mono<PaymentEvent> findPaymentEventMono = paymentPersistentAdapter.findById(paymentEvent.getId());
		PaymentEvent findPaymentEvent = findPaymentEventMono.block();

		assertEquals(paymentEvent.getId(), findPaymentEvent.getId(), "저장된 객체가 같지 않습니다");
		assertEquals(1, findPaymentEvent.getOrders().size());
		assertEquals(1, findPaymentEvent.getCoupons().size());
	}

	@Test
	void vaild_검증(){
		Mono<Boolean> vaild = paymentPersistentAdapter.isValid(paymentEvent.getCheckoutId(), paymentEvent.getTotalAmount());
		StepVerifier.create(vaild)
			.assertNext(
				it -> assertEquals(true, it)
			).verifyComplete();
	}


	@Test
	void checkoutId로_payment_검사(){
		Mono<PaymentEvent> paymentByCheckoutId = paymentPersistentAdapter.getPaymentByCheckoutId(paymentEvent.getCheckoutId());
		StepVerifier.create(paymentByCheckoutId)
			.assertNext(
				it -> {
					assertEquals(paymentEvent.getCheckoutId(), it.getCheckoutId());
					assertEquals(paymentEvent.getTotalAmount(), it.getTotalAmount());
					assertEquals(paymentEvent.getCreatedAt(), it.getCreatedAt());
				}
			).verifyComplete();
	}
}
