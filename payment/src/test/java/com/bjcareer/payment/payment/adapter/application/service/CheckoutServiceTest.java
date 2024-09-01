package com.bjcareer.payment.payment.adapter.application.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import com.bjcareer.payment.application.domain.CheckoutResult;
import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.application.port.in.CheckoutCommand;
import com.bjcareer.payment.application.port.out.LoadCouponPort;
import com.bjcareer.payment.application.port.out.LoadProductPort;
import com.bjcareer.payment.application.port.out.SavePaymentPort;
import com.bjcareer.payment.application.service.CheckoutService;

import reactor.core.publisher.Mono;

class CheckoutServiceTest {
	public static final long ID = 1L;
	public static final int PERCENTAGE = 20;
	public static final String TEST_BUYER = "test_buyer";
	@Mock private LoadProductPort loadProductPort;
	@Mock private LoadCouponPort loadCouponPort;
	@Mock private SavePaymentPort savePaymentPort;
	private CheckoutService checkoutUsecase;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Mockito 애노테이션을 초기화합니다.
		checkoutUsecase = new CheckoutService(loadProductPort, loadCouponPort, savePaymentPort);
	}

	@Test
	void PaymentEvent를_orderId를_받아올_수_있는지_테스트함(){

		List<PaymentCoupon> paymentCoupons = List.of(new PaymentCoupon(ID, PERCENTAGE));
		List<PaymentOrder> paymentOrder = List.of(new PaymentOrder(ID));
		CheckoutCommand checkoutCommand = new CheckoutCommand(ID, TEST_BUYER, List.of(ID), List.of(ID));

		PaymentEvent paymentEvent = new PaymentEvent(ID, TEST_BUYER, checkoutCommand.getIdempotenceKey(), paymentOrder, paymentCoupons);
		when(savePaymentPort.save(any())).thenReturn(Mono.just(paymentEvent));

		Mono<CheckoutResult> checkoutmono = checkoutUsecase.checkout(checkoutCommand);
		CheckoutResult checkout = checkoutmono.block();

		assertEquals(checkoutCommand.getIdempotenceKey() , checkout.getOrderId());
		assertEquals(1, checkout.getAmount());
	}

	@Test
	void PaymentEvent는_반듯이_한번만_성공해야함(){

		List<PaymentCoupon> paymentCoupons = List.of(new PaymentCoupon(ID, PERCENTAGE));
		List<PaymentOrder> paymentOrder = List.of(new PaymentOrder(ID));
		CheckoutCommand checkoutCommand = new CheckoutCommand(ID, TEST_BUYER, List.of(ID), List.of(ID));

		PaymentEvent paymentEvent = new PaymentEvent(ID, TEST_BUYER, checkoutCommand.getIdempotenceKey(), paymentOrder, paymentCoupons);
		when(savePaymentPort.save(any())).thenReturn(Mono.just(paymentEvent));

		Mono<CheckoutResult> checkoutmono = checkoutUsecase.checkout(checkoutCommand);
		CheckoutResult checkout = checkoutmono.block();

		assertEquals(checkoutCommand.getIdempotenceKey() , checkout.getOrderId());
		assertEquals(1, checkout.getAmount());
	}

}