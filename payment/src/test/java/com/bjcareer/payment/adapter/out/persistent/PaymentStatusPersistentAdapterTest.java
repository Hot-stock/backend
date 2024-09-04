package com.bjcareer.payment.adapter.out.persistent;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;
import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;
import com.bjcareer.payment.helper.HelperPayment;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class PaymentStatusPersistentAdapterTest {
	@Autowired PaymentPersistentAdapter paymentPersistentAdapter;
	@Autowired PaymentStatusPersistentAdapter paymentStatusPersistentAdapter;

	PaymentEvent paymentEvent;

	@BeforeEach
	void setUp() {
		paymentEvent = HelperPayment.createPayment();
		paymentEvent = paymentPersistentAdapter.save(paymentEvent).block();
	}

	@AfterEach
	void tearDown() {
		paymentPersistentAdapter.delete(paymentEvent).block();
	}

	@Test
	void paymentEvent에_있는_주문들을_시작_상태로_변경_되고_order_history에도_기록되어야_함() throws InterruptedException {
		String expectedPaymentKey = "TEST_PAYMENT_KEY";
		//when
		PaymentStatusUpdateCommand cmd = new PaymentStatusUpdateCommand(paymentEvent.getCheckoutId(), PaymentStatus.EXECUTING, LocalDateTime.now(), "TEST_PAYMENT_EXECUTING");
		paymentEvent = paymentStatusPersistentAdapter.updatePaymentStatusToExecuting(cmd, expectedPaymentKey).block();
		PaymentEvent findPayment = paymentPersistentAdapter.getPaymentByCheckoutId(paymentEvent.getCheckoutId()).block();

		assertEquals(expectedPaymentKey, findPayment.getPaymentKey(), "PAYMENT_KEY가 설정되지 않았습니다.");
		findPayment.getOrders().forEach(order -> assertEquals(PaymentStatus.EXECUTING, order.getPaymentStatus(), "Order 상태가 EXECUTING으로 설정되지 않았습니다."));

	}

	@Test
	void order_status가_succes로_변경되는지() {
		//when
		PaymentStatusUpdateCommand cmd = new PaymentStatusUpdateCommand(paymentEvent.getCheckoutId(), PaymentStatus.SUCCESS, LocalDateTime.now(), "TEST_PAYMENT_EXECUTING");
		Mono<Boolean> booleanMono = paymentStatusPersistentAdapter.updatePaymentStatus(cmd);

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
			assertEquals(cmd.getApprovedAt(), order.getUpdatedAt(), "Order 상태가 SUCCESS로 설정되지 않았습니다.")
		);
	}

}