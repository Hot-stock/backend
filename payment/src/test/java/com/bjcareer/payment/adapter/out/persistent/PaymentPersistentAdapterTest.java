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

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class PaymentPersistentAdapterTest {
	long PRODUCT_ID = 1L;
	long COUPON_ID = 1L;
	String BUYER_ID = "USER_1";
	int AMOUNT = 2000;
	int PERCENTAGE = 20;

	String CHECKOUT_ID = UUID.randomUUID().toString();

	@Autowired
	PaymentPersistentAdapter paymentPersistentAdapter;
	PaymentEvent paymentEvent;

	@BeforeEach
	void setUp() {
		PaymentOrder paymentOrder = new PaymentOrder(PRODUCT_ID, AMOUNT);
		PaymentCoupon paymentCoupon = new PaymentCoupon(COUPON_ID, PERCENTAGE);
		paymentEvent = new PaymentEvent(BUYER_ID, CHECKOUT_ID, List.of(paymentOrder), List.of(paymentCoupon));

		// Act: 실제로 저장 메서드를 호출하고 결과를 얻습니다.
		Mono<PaymentEvent> save = paymentPersistentAdapter.save(paymentEvent);
		paymentEvent = save.block();

	}

	@AfterEach
	void tearDown(){
		Mono<Void> delete = paymentPersistentAdapter.delete(paymentEvent);
		delete.block();
	}

	@Test
	void Payment_이벤트_객체를_단건_저장() {
		int percentage = 20;
		int amount = 100;

		PaymentOrder paymentOrder = new PaymentOrder(PRODUCT_ID, amount);
		PaymentCoupon paymentCoupon = new PaymentCoupon(COUPON_ID, percentage);
		PaymentEvent paymentEvent = new PaymentEvent(BUYER_ID, UUID.randomUUID().toString(), List.of(paymentOrder), List.of(paymentCoupon));

		// Act: 실제로 저장 메서드를 호출하고 결과를 얻습니다.
		Mono<PaymentEvent> save = paymentPersistentAdapter.save(paymentEvent);
		PaymentEvent target = save.block();

		assertNotNull(target.getId());
		assertEquals(1, target.getOrders().size());
		assertEquals(1, target.getCoupons().size());

		paymentPersistentAdapter.delete(target);
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
		Mono<Boolean> vaild = paymentPersistentAdapter.isVaild(CHECKOUT_ID, paymentEvent.getTotalAmount());
		StepVerifier.create(vaild)
			.assertNext(
				it -> assertEquals(true, it)
			).verifyComplete();
	}


	@Test
	void checkoutId로_payment_검사(){
		Mono<PaymentEvent> paymentByCheckoutId = paymentPersistentAdapter.getPaymentByCheckoutId(CHECKOUT_ID);
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
