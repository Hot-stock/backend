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
import static org.junit.jupiter.api.Assertions.*;

import reactor.core.publisher.Mono;

@SpringBootTest
class PaymentConfirmServiceTest {
	String ORDER_ID = UUID.randomUUID().toString();
	String PAYMENT_KEY = UUID.randomUUID().toString();
	long AMOUNT = 1000;

	@Autowired
	PaymentPersistentAdapter paymentPersistentAdapter;
	@Autowired
	PaymentStatusPersistentAdapter paymentStatusPersistentAdapter;
	@Autowired
	PaymentConfirmService paymentConfirmService;

	PaymentEvent paymentEvent;

	@BeforeEach
	void setUp() {
		long ID = 1L;
		int PERCENTAGE = 20;

		List<PaymentOrder> paymentOrders = List.of(new PaymentOrder(ID, (int)AMOUNT));
		List<PaymentCoupon> paymentCoupons = List.of(new PaymentCoupon(ID, PERCENTAGE));

		paymentEvent = new PaymentEvent("BUYER_ID", ORDER_ID, paymentOrders, paymentCoupons);
		paymentPersistentAdapter.save(paymentEvent).block();
	}

	@AfterEach
	void tearDown() {
		paymentPersistentAdapter.delete(paymentEvent).block();
	}

	@Test
	void Confirm_결제_요청(){
		PaymentConfirmCommand command = new PaymentConfirmCommand(PAYMENT_KEY, ORDER_ID, AMOUNT);
		PaymentExecutionResult expectedResult = new PaymentExecutionResult(PAYMENT_KEY, ORDER_ID, "ORDER_NAME", 1, "DONE",
			"2022-01-01T00:00:00+09:00", "2022-01-01T00:00:00+09:00", true, false, false);

		Mono<PaymentExecutionResult> confirm = paymentConfirmService.confirm(command);
		PaymentExecutionResult result = confirm.block();

		assertEquals(expectedResult, result, "예상된 결과가 같지 않습니다");
	}
}