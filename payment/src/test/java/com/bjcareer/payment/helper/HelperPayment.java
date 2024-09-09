package com.bjcareer.payment.helper;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.bjcareer.payment.adapter.out.persistent.PaymentPersistentAdapter;
import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;

import reactor.core.publisher.Mono;

public class HelperPayment {
	public static final long PRODUCT_ID = 1L;
	public static final long COUPON_ID = 1L;
	public static final int AMOUNT = 2000;
	public static final int PERCENTAGE = 20;

	public static final String PAYMENT_KEY = "TEST_PAYMENT_KEY";
	public static String CHECKOUT_ID = UUID.randomUUID().toString();
	public static String BUYER_ID = UUID.randomUUID().toString();



	public static PaymentEvent createPayment() {
		CHECKOUT_ID = UUID.randomUUID().toString();
		BUYER_ID = UUID.randomUUID().toString();

		PaymentOrder paymentOrder = new PaymentOrder(PRODUCT_ID, AMOUNT);
		PaymentCoupon paymentCoupon = new PaymentCoupon(COUPON_ID, PERCENTAGE);
		PaymentEvent paymentEvent = new PaymentEvent(BUYER_ID, CHECKOUT_ID, List.of(paymentOrder),
			List.of(paymentCoupon));
		paymentEvent.updatePaymentKey(PAYMENT_KEY);

		return paymentEvent;
	}
}
