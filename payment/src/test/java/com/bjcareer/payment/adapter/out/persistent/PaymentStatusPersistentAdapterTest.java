package com.bjcareer.payment.adapter.out.persistent;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;
import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class PaymentStatusPersistentAdapterTest {
	@Autowired PaymentPersistentAdapter paymentPersistentAdapter;
	@Autowired PaymentStatusPersistentAdapter paymentStatusPersistentAdapter;

	PaymentEvent paymentEvent;

	@BeforeEach
	void setUp() {
		String ORDER_ID = UUID.randomUUID().toString();
		long ID = 1L;
		int PERCENTAGE = 20;
		int AMOUNT = 20000;

		List<PaymentOrder> paymentOrders = List.of(new PaymentOrder(ID, AMOUNT));
		List<PaymentCoupon> paymentCoupons = List.of(new PaymentCoupon(ID, PERCENTAGE));

		paymentEvent = new PaymentEvent("BUYER_ID", ORDER_ID, paymentOrders, paymentCoupons);
		paymentPersistentAdapter.save(paymentEvent).block();
	}

	// @AfterEach
	void tearDown() {
		paymentPersistentAdapter.delete(paymentEvent).block();
	}

	@Test
	void order의_상태들을_실행으로_변경() {
		String expectedPaymentKey = "TEST_PAYMENT_KEY";

		Mono<Void> result = paymentStatusPersistentAdapter.updatePaymentStatusToExecuting(paymentEvent.getOrderId(),
			expectedPaymentKey);

		StepVerifier.create(result).verifyComplete();  // 시퀀스가 정상적으로 종료되었는지 확인

		Mono<PaymentEvent> reloadResult = paymentPersistentAdapter.findById(paymentEvent.getId());

		StepVerifier.create(reloadResult)
			.assertNext(paymentEvent -> {
				assertEquals(expectedPaymentKey, paymentEvent.getPaymentKey(), "PAYMENT_KEY가 설정되지 않았습니다.");

				paymentEvent.getOrders().forEach(order ->
					assertEquals(PaymentStatus.EXECUTING, order.getPaymentStatus(), "Order 상태가 EXECUTING으로 설정되지 않았습니다.")
				);
			})
			.verifyComplete();  // 시퀀스가 정상적으로 종료되었는지 확인
	}

	@Test
	void order_status가_succes로_변경되는지() {
		PaymentStatusUpdateCommand command = new PaymentStatusUpdateCommand(paymentEvent.getOrderId(), PaymentStatus.SUCCESS, LocalDateTime.now());

		Mono<Boolean> booleanMono = paymentStatusPersistentAdapter.updatePaymentStatus(command);

		StepVerifier.create(booleanMono)
			.expectNext(true)  // updatePaymentStatus가 성공적으로 완료되었는지 확인
			.verifyComplete();  // Mono<Boolean>이 성공적으로 완료되었는지 확인

		// PaymentEvent를 다시 로드하여 상태를 확인
		PaymentEvent updatedPaymentEvent = paymentPersistentAdapter.findById(paymentEvent.getId()).block();
		assertNotNull(updatedPaymentEvent, "Updated PaymentEvent가 null입니다.");
		assertEquals(true, updatedPaymentEvent.isPaymentDone(), "결제 완료처리가 되지 않았습니다.");

		// 각 Order의 상태가 SUCCESS로 업데이트되었는지 확인
		updatedPaymentEvent.getOrders().forEach(order ->
			assertEquals(PaymentStatus.SUCCESS, order.getPaymentStatus(), "Order 상태가 SUCCESS로 설정되지 않았습니다.")
		);
		// 각 Order의 상태가 SUCCESS로 업데이트되었는지 확인
		updatedPaymentEvent.getOrders().forEach(order ->
			assertEquals(command.getApprovedAt(), order.getUpdatedAt(), "Order 상태가 SUCCESS로 설정되지 않았습니다.")
		);
	}

}