package com.bjcareer.payment.adapter.out.web;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.payment.adapter.out.persistent.PaymentPersistentAdapter;
import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.application.port.out.PaymentExecutionPort;

@SpringBootTest
class PaymentExecutionWebAdapterTest {
	String ORDER_ID = UUID.randomUUID().toString();

	@Autowired
	PaymentExecutionPort paymentExecutionPort;
	@Autowired
	PaymentPersistentAdapter paymentPersistentAdapter;

	PaymentEvent paymentEvent;


	@BeforeEach
	void setUp() {
		long ID = 1L;
		int PERCENTAGE = 20;
		int AMOUNT = 20000;

		List<PaymentOrder> paymentOrders = List.of(new PaymentOrder(ID, AMOUNT));
		List<PaymentCoupon> paymentCoupons = List.of(new PaymentCoupon(ID, PERCENTAGE));

		paymentEvent = new PaymentEvent("BUYER_ID", ORDER_ID, paymentOrders, paymentCoupons);
		paymentPersistentAdapter.save(paymentEvent).block();
	}

	@AfterEach
	void tearDown() {
		paymentPersistentAdapter.delete(paymentEvent).block();
	}

	@Test
	void vaild검증(){

	}

}