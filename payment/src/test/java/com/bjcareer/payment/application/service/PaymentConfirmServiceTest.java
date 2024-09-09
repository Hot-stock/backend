package com.bjcareer.payment.application.service;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.payment.adapter.out.persistent.PaymentPersistentAdapter;
import com.bjcareer.payment.adapter.out.web.psp.PaymentExecutionWebAdapter;
import com.bjcareer.payment.adapter.out.web.psp.toss.exception.TossErrorCode;
import com.bjcareer.payment.adapter.out.web.psp.toss.executor.TossPaymentExecutor;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;
import com.bjcareer.payment.application.port.in.PaymentConfirmCommand;
import com.bjcareer.payment.application.port.out.PaymentExecutionPort;
import com.bjcareer.payment.application.port.out.PaymentStatusUpdatePort;
import com.bjcareer.payment.application.port.out.PaymentValidationPort;
import com.bjcareer.payment.helper.HelperPayment;
import com.bjcareer.payment.helper.PSPWebClientConfiguration;

import static org.junit.jupiter.api.Assertions.*;

import reactor.core.publisher.Mono;

@SpringBootTest
class PaymentConfirmServiceTest {
	@Autowired
	private PSPWebClientConfiguration webClientConfiguration;

	@Autowired
	private PaymentStatusUpdatePort paymentStatusUpdatePort;

	@Autowired
	private PaymentValidationPort paymentValidationPort;

	@Autowired
	private PaymentPersistentAdapter paymentPersistentAdapter;

	private PaymentExecutionPort paymentExecutionPort;
	private PaymentEvent paymentEvent;
	private PaymentConfirmCommand command;

	@BeforeEach
	void setUp() {
		paymentEvent = HelperPayment.createPayment();
		paymentEvent = paymentPersistentAdapter.save(paymentEvent).block();
		command = new PaymentConfirmCommand(paymentEvent.getPaymentKey(), paymentEvent.getCheckoutId(), paymentEvent.getTotalAmount());
	}

	@AfterEach
	void tearDown() {
		paymentPersistentAdapter.getPaymentByCheckoutId(paymentEvent.getCheckoutId())
			.flatMap(payment -> payment != null ? paymentPersistentAdapter.delete(payment) : Mono.empty())
			.onErrorResume(e -> Mono.empty())
			.block();
	}

	@Test
	void shouldFailDueToExceedingLimit_AndUpdateOrderHistory() {
		setupExecutionPort(TossErrorCode.EXCEED_MAX_AMOUNT);

		PaymentConfirmService paymentConfirmService = createPaymentConfirmService();
		Mono<PaymentExecutionResult> confirm = paymentConfirmService.confirm(command);

		confirm.onErrorResume(throwable -> Mono.empty()).block();
		verifyPaymentStatus(PaymentStatus.FAILURE);
	}

	@Test
	void shouldFailDueToInvalidPaymentValidation() {
		command = new PaymentConfirmCommand(paymentEvent.getPaymentKey(), paymentEvent.getCheckoutId(), 2L);

		PaymentConfirmService paymentConfirmService = createPaymentConfirmService();
		Mono<PaymentExecutionResult> confirm = paymentConfirmService.confirm(command);

		confirm.onErrorResume(throwable -> Mono.empty()).block();
		verifyPaymentStatus(PaymentStatus.FAILURE);
	}

	@Test
	void shouldCompleteSuccessfully_IfAlreadyApproved() {
		setupExecutionPort(TossErrorCode.ALREADY_COMPLETED_PAYMENT);

		PaymentConfirmService paymentConfirmService = createPaymentConfirmService();
		Mono<PaymentExecutionResult> confirm = paymentConfirmService.confirm(command);

		confirm.onErrorResume(throwable -> Mono.empty()).block();
		verifyPaymentStatus(PaymentStatus.SUCCESS);
	}

	private void setupExecutionPort(TossErrorCode errorCode) {
		WebClient webClient = webClientConfiguration.tossPaymentWebClient(errorCode.toString());
		TossPaymentExecutor executor = new TossPaymentExecutor(webClient);
		paymentExecutionPort = new PaymentExecutionWebAdapter(executor);
	}

	private PaymentConfirmService createPaymentConfirmService() {
		return new PaymentConfirmService(paymentStatusUpdatePort, paymentValidationPort, paymentExecutionPort);
	}

	private void verifyPaymentStatus(PaymentStatus expectedStatus) {
		PaymentEvent result = paymentPersistentAdapter.getPaymentByCheckoutId(paymentEvent.getCheckoutId()).block();
		List<PaymentOrder> orders = result.getOrders();

		assertTrue(result.isPaymentDone());
		assertEquals(paymentEvent.getPaymentKey(), result.getPaymentKey());
		orders.forEach(order -> assertEquals(expectedStatus, order.getPaymentStatus()));
	}
}
