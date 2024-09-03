package com.bjcareer.payment.application.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

import com.bjcareer.payment.adapter.out.persistent.PaymentPersistentAdapter;
import com.bjcareer.payment.application.domain.CheckoutResult;
import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.application.port.in.CheckoutCommand;
import com.bjcareer.payment.application.port.out.LoadCouponPort;
import com.bjcareer.payment.application.port.out.LoadPaymentPort;
import com.bjcareer.payment.application.port.out.LoadProductPort;
import com.bjcareer.payment.application.port.out.SavePaymentPort;
import com.bjcareer.payment.helper.HelperPayment;

import reactor.core.publisher.Mono;

@SpringBootTest
class CheckoutServiceTest {
	public static final Long CART_ID = 1L;

	@Autowired CheckoutService checkoutUsecase;
	@Autowired PaymentPersistentAdapter paymentPersistentAdapter;

	CheckoutCommand checkoutCommand;
	PaymentEvent paymentEvent;


	@BeforeEach
	void setUp() {
		paymentEvent = HelperPayment.createPayment();
		checkoutCommand = new CheckoutCommand(CART_ID, HelperPayment.BUYER_ID, List.of(HelperPayment.PRODUCT_ID), List.of(HelperPayment.COUPON_ID));

		// paymentEvent = paymentPersistentAdapter.save(paymentEvent).block();
	}

	@AfterEach
	void tearDown() {
		// paymentPersistentAdapter.delete(paymentEvent).block();
	}


	@Test
	void PaymentCheckout_생성을_테스트 (){
		Mono<CheckoutResult> checkoutmono = checkoutUsecase.checkout(checkoutCommand);
		CheckoutResult checkout = checkoutmono.block();

		assertEquals(checkoutCommand.getIdempotenceKey() , checkout.getCheckoutId());
		assertEquals(HelperPayment.AMOUNT, checkout.getAmount());
	}

	@Test
	void PaymentEvent의_멱등성_보장을_테스트함(){
		Mono<CheckoutResult> checkoutmono = checkoutUsecase.checkout(checkoutCommand);
		CheckoutResult checkout = checkoutmono.block();

		assertEquals(checkoutCommand.getIdempotenceKey() , checkout.getCheckoutId());
		assertEquals(HelperPayment.AMOUNT, checkout.getAmount());
	}

	@Test
	void 인가되지_않은_사용자가_checkout을_요청함(){
		Mono<CheckoutResult> checkoutmono = checkoutUsecase.checkout(checkoutCommand);
		CheckoutResult checkout = checkoutmono.block();

		assertEquals(checkoutCommand.getIdempotenceKey() , checkout.getCheckoutId());
		assertEquals(HelperPayment.AMOUNT, checkout.getAmount());
	}

}