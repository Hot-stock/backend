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
		paymentPersistentAdapter.delete(paymentEvent).block();
	}

	@Test
	void Confirm_결제_요청(){
		PaymentExecutionResult expectedResult = new PaymentExecutionResult(paymentEvent.getPaymentKey(), paymentEvent.getCheckoutId(), "ORDER_NAME", 1, "DONE",
			"2022-01-01T00:00:00+09:00", "2022-01-01T00:00:00+09:00", true, false, false);

		Mono<PaymentExecutionResult> confirm = paymentConfirmService.confirm(command);
		PaymentExecutionResult result = confirm.block();

		assertEquals(expectedResult, result, "예상된 결과가 같지 않습니다");
	}
}