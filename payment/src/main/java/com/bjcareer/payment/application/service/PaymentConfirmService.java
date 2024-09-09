package com.bjcareer.payment.application.service;

import org.springframework.stereotype.Component;

import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;
import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;
import com.bjcareer.payment.application.port.in.PaymentConfirmCommand;
import com.bjcareer.payment.application.port.in.PaymentConfirmUsecase;
import com.bjcareer.payment.application.port.out.PaymentExecutionPort;
import com.bjcareer.payment.application.port.out.PaymentStatusUpdatePort;
import com.bjcareer.payment.application.port.out.PaymentValidationPort;
import com.bjcareer.payment.application.service.exceptions.PaymentValidationException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PaymentConfirmService implements PaymentConfirmUsecase {

	private final PaymentStatusUpdatePort paymentStatusUpdatePort;
	private final PaymentValidationPort paymentValidationPort;
	private final PaymentExecutionPort paymentExecutionPort;

	@Override
	public Mono<PaymentExecutionResult> confirm(PaymentConfirmCommand command) {
		return validatePayment(command)
			.flatMap(valid -> {
				if (valid) {
					return processPayment(command);
				}
				return Mono.error(new PaymentValidationException("요청한 결제 정보가 잘못됐습니다"));
			});
	}

	private Mono<Boolean> validatePayment(PaymentConfirmCommand command) {
		return paymentValidationPort.isVaild(command.getCheckoutId(), command.getAmount())
			.doOnNext(valid -> System.out.println("valid = " + valid));
	}

	private Mono<PaymentExecutionResult> processPayment(PaymentConfirmCommand command) {
		return updatePaymentStatusToExecuting(command)
			.flatMap(this::executePayment)
			.flatMap(this::finalizePaymentStatus);
	}

	private Mono<PaymentConfirmResult> updatePaymentStatusToExecuting(PaymentConfirmCommand command) {
		return paymentStatusUpdatePort.updatePaymentStatusToExecuting(command.getCheckoutId(), command.getPaymentKey())
			.thenReturn(new PaymentConfirmResult(command.getPaymentKey(), command.getCheckoutId(), command.getAmount()));
	}

	private Mono<PaymentExecutionResult> executePayment(PaymentConfirmResult confirmResult) {
		return paymentExecutionPort.execute(confirmResult);
	}

	private Mono<PaymentExecutionResult> finalizePaymentStatus(PaymentExecutionResult executionResult) {
		return paymentStatusUpdatePort.updatePaymentStatus(
				new PaymentStatusUpdateCommand(executionResult.getCheckoutId(), executionResult.getStatus(), executionResult.getApprovedAt()))
			.thenReturn(executionResult);
	}
}
