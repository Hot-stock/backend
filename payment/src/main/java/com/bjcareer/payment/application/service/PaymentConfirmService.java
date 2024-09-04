package com.bjcareer.payment.application.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bjcareer.payment.adapter.out.web.toss.exception.PspConfirmationException;
import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;
import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;
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

	private static final Logger log = LoggerFactory.getLogger(PaymentConfirmService.class);
	private final PaymentStatusUpdatePort paymentStatusUpdatePort;
	private final PaymentValidationPort paymentValidationPort;
	private final PaymentExecutionPort paymentExecutionPort;

	@Override
	public Mono<PaymentExecutionResult> confirm(PaymentConfirmCommand command) {
		return validatePayment(command)
			.filter(isValid -> isValid)
			.flatMap(isValid -> processPayment(command))
			.onErrorResume(throwable -> {
				PaymentStatusUpdateCommand updateCmd;
				if (throwable instanceof PaymentValidationException) {
					updateCmd = createUpdateStatusCommandWhenErrorRaise(command.getCheckoutId(), PaymentStatus.FAILURE, throwable.getMessage());
				} else if (throwable instanceof PspConfirmationException pspException) {
					updateCmd = createUpdateStatusCommandWhenErrorRaise(command.getCheckoutId(), pspException.getPaymentStatus(),
						pspException.getErrrMsg());
				} else if (throwable instanceof TimeoutException) {
					updateCmd = createUpdateStatusCommandWhenErrorRaise(command.getCheckoutId(), PaymentStatus.EXECUTING,
						"TimeoutException");
				}else{
					updateCmd = createUpdateStatusCommandWhenErrorRaise(command.getCheckoutId(), PaymentStatus.UNKNOWN, throwable.getMessage());
				}
				return paymentStatusUpdatePort.updatePaymentStatus(updateCmd)
					.then(Mono.error(throwable));
			});
	}

	private Mono<Boolean> validatePayment(PaymentConfirmCommand command) {
		return paymentValidationPort.isValid(command.getCheckoutId(), command.getAmount())
			.filterWhen(valid -> {
				if (!valid) {
					return Mono.error(new PaymentValidationException("요청한 결제 정보가 잘못됐습니다"));
				}
				return Mono.just(true);
			});
	}

	private Mono<PaymentExecutionResult> processPayment(PaymentConfirmCommand command) {
		return updatePaymentStatusToExecuting(command)
			.flatMap(this::executePayment)
			.flatMap(this::finalizePaymentStatus);
	}

	private Mono<PaymentConfirmResult> updatePaymentStatusToExecuting(PaymentConfirmCommand command) {
		PaymentStatusUpdateCommand updateCommand = new PaymentStatusUpdateCommand(command.getCheckoutId(), PaymentStatus.EXECUTING, LocalDateTime.now(), "Confirm 요청");
		return paymentStatusUpdatePort.updatePaymentStatusToExecuting(updateCommand, command.getPaymentKey())
			.thenReturn(new PaymentConfirmResult(command.getPaymentKey(), command.getCheckoutId(), command.getAmount()));
	}

	private Mono<PaymentExecutionResult> executePayment(PaymentConfirmResult confirmResult) {
		return paymentExecutionPort.execute(confirmResult);
	}

	private Mono<PaymentExecutionResult> finalizePaymentStatus(PaymentExecutionResult executionResult) {
		PaymentStatusUpdateCommand updateCommand = new PaymentStatusUpdateCommand(executionResult.getCheckoutId(), executionResult.getStatus(), executionResult.getApprovedAt(), "Confirm 응답 기록");
		return paymentStatusUpdatePort.updatePaymentStatus(updateCommand)
			.thenReturn(executionResult);
	}

	public PaymentStatusUpdateCommand createUpdateStatusCommandWhenErrorRaise(String checkoutId, PaymentStatus status, String reason) {
		return new PaymentStatusUpdateCommand(checkoutId, status, LocalDateTime.now(), reason);
	}
}
