package com.bjcareer.payment.application.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bjcareer.payment.adapter.out.web.psp.exceptions.PspConfirmationException;
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
	private final PaymentConfirmErrorHandlingService paymentErrorHandlingService;

	@Override
	public Mono<PaymentExecutionResult> confirm(PaymentConfirmCommand command) {

		return paymentValidationPort.isValid(command.getCheckoutId(), command.getAmount())
			.filter(isValid -> isValid)
			.switchIfEmpty(Mono.error(new PaymentValidationException("요청한 결제 정보가 잘못됐습니다")))  // 조건이 false일 때 에러 발생
			.flatMap(isValid -> processPayment(command)); //processPayment는 항상  PspConfirmationExceptiond만 riase함
	}

	//processPayment는 에러 처리 후 PaymentExecutionResult만 던짐
	private Mono<PaymentExecutionResult> processPayment(PaymentConfirmCommand command) {
		return updatePaymentStatusToExecuting(command)
			.flatMap(this::executePayment)
			.flatMap(this::finalizePaymentStatus)
			.onErrorResume(PspConfirmationException.class, throwable -> {
				PaymentStatusUpdateCommand updateCommand = paymentErrorHandlingService.handle(throwable, command);
				paymentStatusUpdatePort.updatePaymentStatus(updateCommand);
				return Mono.just(new PaymentExecutionResult(command, throwable));  // 예외 발생 시 처리
			});
	}

	private Mono<PaymentConfirmResult> updatePaymentStatusToExecuting(PaymentConfirmCommand command) {
		PaymentStatusUpdateCommand updateCommand = new PaymentStatusUpdateCommand(command.getCheckoutId(), PaymentStatus.EXECUTING, LocalDateTime.now(), "Confirm 요청", command.getPaymentKey());
		return paymentStatusUpdatePort.updatePaymentStatus(updateCommand)
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
}
