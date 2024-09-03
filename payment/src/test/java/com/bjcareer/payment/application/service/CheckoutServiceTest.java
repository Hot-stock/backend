package com.bjcareer.payment.application.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.payment.adapter.out.persistent.PaymentPersistentAdapter;
import com.bjcareer.payment.application.domain.CheckoutResult;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.port.in.CheckoutCommand;
import com.bjcareer.payment.application.port.in.ValidationCheckoutCommand;
import com.bjcareer.payment.application.service.exceptions.AuthenticationFailureException;
import com.bjcareer.payment.application.service.exceptions.DuplicatedCheckout;
import com.bjcareer.payment.helper.HelperPayment;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class CheckoutServiceTest {
	Random random = new Random();
	Long CART_ID = random.nextLong(1000);

	@Autowired CheckoutService checkoutUsecase;
	@Autowired PaymentPersistentAdapter paymentPersistentAdapter;

	CheckoutCommand checkoutCommand;
	PaymentEvent paymentEvent;


	@BeforeEach
	void setUp() {
		paymentEvent = HelperPayment.createPayment();
		checkoutCommand = new CheckoutCommand(CART_ID, HelperPayment.BUYER_ID, List.of(HelperPayment.PRODUCT_ID), List.of(HelperPayment.COUPON_ID));
	}

	@AfterEach
	void tearDown() {
		paymentPersistentAdapter.getPaymentByCheckoutId(paymentEvent.getCheckoutId())
			.doOnSuccess(payment -> {
				if (payment != null) {
					paymentPersistentAdapter.delete(payment).block(); // 삭제 작업을 동기적으로 수행
				}
			})
			.block(); // 비동기 흐름을 동기적으로 마무리
	}

	@Test
	void PaymentCheckout_생성_성공(){
		Mono<CheckoutResult> checkoutmono = checkoutUsecase.checkout(checkoutCommand);
		CheckoutResult checkout = checkoutmono.block();

		assertEquals(checkoutCommand.getIdempotenceKey() , checkout.getCheckoutId());
		assertEquals(HelperPayment.AMOUNT, Integer.parseInt(checkout.getAmount()));
	}

	@Test
	void PaymentEvent의_멱등성_보장을_해야함으로_DB에_저장될_수_없음() {
		// 첫번째 저장
		checkoutUsecase.checkout(checkoutCommand).block();
		//두번째 저장 실패가 나와야 함
		Mono<CheckoutResult> creator = checkoutUsecase.checkout(checkoutCommand);

		StepVerifier.create(creator)
			.expectError(DuplicatedCheckout.class) // 예외가 발생할 것을 기대
			.verify();
	}


	@Test
	void 인가된_사용자가_checkout을_요청함(){
		CheckoutResult result = checkoutUsecase.checkout(checkoutCommand).block();
		CheckoutResult checkout = checkoutUsecase.validationCheckout(new ValidationCheckoutCommand(HelperPayment.BUYER_ID, result.getCheckoutId())).block();

		assertEquals(checkoutCommand.getIdempotenceKey() , checkout.getCheckoutId());
	}

	@Test
	void 인가되지_않은_사용자가_checkout을_요청함(){
		CheckoutResult result = checkoutUsecase.checkout(checkoutCommand).block();
		Mono<CheckoutResult> creator = checkoutUsecase.validationCheckout(
			new ValidationCheckoutCommand("NOT+CHECKOUT_USER", result.getCheckoutId()));

		StepVerifier.create(creator)
			.expectError(AuthenticationFailureException.class) // 예외가 발생할 것을 기대
			.verify();
	}

}