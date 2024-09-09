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
import com.bjcareer.payment.helper.HelperPayment;

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
		paymentEvent = HelperPayment.createPayment();
		paymentEvent = paymentPersistentAdapter.save(paymentEvent).block();
	}

	@AfterEach
	void tearDown() {
		paymentPersistentAdapter.delete(paymentEvent).block();
	}

	@Test
	void vaild검증(){

	}

}