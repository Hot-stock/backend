package com.bjcareer.payment.application.service;


import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.payment.adapter.out.persistent.PaymentPersistentAdapter;
import com.bjcareer.payment.adapter.out.persistent.PaymentStatusPersistentAdapter;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;
import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.application.port.in.PaymentConfirmCommand;
import com.bjcareer.payment.helper.HelperPayment;

import static org.junit.jupiter.api.Assertions.*;

import reactor.core.publisher.Mono;

@SpringBootTest
class PaymentConfirmServiceTest {
	@Autowired
	PaymentPersistentAdapter paymentPersistentAdapter;
	@Autowired
	PaymentStatusPersistentAdapter paymentStatusPersistentAdapter;
	@Autowired PaymentConfirmService paymentConfirmService;

	PaymentEvent paymentEvent;
	PaymentConfirmCommand command;

	@BeforeEach
	void setUp() {
		paymentEvent = HelperPayment.createPayment();
		paymentEvent = paymentPersistentAdapter.save(paymentEvent).block();

		command = new PaymentConfirmCommand(paymentEvent.getPaymentKey(), paymentEvent.getCheckoutId(), paymentEvent.getTotalAmount());
	}

	@AfterEach
	void tearDown() {
		paymentPersistentAdapter.getPaymentByCheckoutId(paymentEvent.getCheckoutId())
			.doOnSuccess(payment -> {
				if (payment != null) {
					paymentPersistentAdapter.delete(payment).block(); // 삭제 작업을 동기적으로 수행
				}
			})
			.onErrorResume(e -> {
				return Mono.empty(); // 에러가
			})
			.block(); // 비동기 흐름을 동기적으로 마무리
	}

	@Test
	void Confirm_결제가_완료되고_나면_orderHistory_order_payment의_내용이_전부_변경되어야함(){
		PaymentExecutionResult expectedResult = new PaymentExecutionResult(paymentEvent.getPaymentKey(), paymentEvent.getCheckoutId(), "ORDER_NAME", 1, "DONE",
			"2022-01-01T00:00:00+09:00", "2022-01-01T00:00:00+09:00", true, false, false);

		Mono<PaymentExecutionResult> confirm = paymentConfirmService.confirm(command);
		PaymentExecutionResult result = confirm.block();

		assertEquals(expectedResult, result, "예상된 결과가 같지 않습니다");
	}
}