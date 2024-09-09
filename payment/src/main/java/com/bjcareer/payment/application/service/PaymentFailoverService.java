package com.bjcareer.payment.application.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bjcareer.payment.adapter.out.web.psp.PspConfirmationException;
import com.bjcareer.payment.application.domain.PaymentConfirmResult;
import com.bjcareer.payment.application.domain.PaymentExecutionResult;
import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;
import com.bjcareer.payment.application.domain.entity.event.PendingPaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;
import com.bjcareer.payment.application.port.in.PaymentFailoverUsecase;
import com.bjcareer.payment.application.port.out.LoadPendingProductPort;
import com.bjcareer.payment.application.port.out.PaymentExecutionPort;
import com.bjcareer.payment.application.port.out.PaymentStatusUpdatePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentFailoverService implements PaymentFailoverUsecase {
	private final LoadPendingProductPort pendingProductPort;
	private final PaymentExecutionPort paymentExecutionPort;
	private final PaymentStatusUpdatePort paymentStatusUpdatePort;

	private final Scheduler FAIL_OVER_SCHEDULER = Schedulers.newSingle("FailOver");

	@Override
	@Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
	public void execute() {
		log.info("Starting payment failover process...");

		pendingProductPort.loadPendingProduct(PaymentStatus.UNKNOWN)
			.flatMap(e -> {
				log.info("Processing PendingPaymentEvent with ID: {}", e.getPaymentEventId());
				return pendingProductPort.loadAmountOfPendingPayment(e.getPaymentEventId().longValue())
					.map(totalAmount -> {
						e.setTotalAmount(totalAmount);
						log.info("Set total amount: {} for PendingPaymentEvent ID: {}", totalAmount, e.getPaymentEventId());
						return e;
					});
			})
			.parallel(2)
			.runOn(Schedulers.parallel())
			.flatMap(pendingPaymentEvent -> {
				log.info("Executing pending order for PaymentEvent ID: {}", pendingPaymentEvent.getPaymentEventId());
				return executePendingOrder(pendingPaymentEvent)
					.flatMap(paymentExecutionResult -> {
						log.info("Finalizing payment status for PaymentEvent ID: {}", pendingPaymentEvent.getPaymentEventId());
						return finalizePaymentStatus(paymentExecutionResult);
					})
					.onErrorResume(throwable -> {
						log.error("Error during execution of PaymentEvent ID: {}. Handling error...", pendingPaymentEvent.getPaymentEventId(), throwable);
						return handleError(throwable, pendingPaymentEvent);
					});
			})
			.sequential()
			.doOnComplete(() -> log.info("Payment failover process completed."))
			.doOnError(error -> log.error("Error occurred during the payment failover process.", error))
			.subscribeOn(FAIL_OVER_SCHEDULER)
			.subscribe();
	}

	private Mono<PaymentExecutionResult> handleError(Throwable throwable, PendingPaymentEvent pendingPaymentEvent) {
		log.error("Handling error for checkout ID: {}", pendingPaymentEvent.getCheckoutId(), throwable);
		pendingPaymentEvent.updateFailCount();
		PaymentStatusUpdateCommand updateCmd;

		if (throwable instanceof PspConfirmationException pspException) {
			log.warn("PspConfirmationException for checkout ID: {}", pendingPaymentEvent.getCheckoutId());
			updateCmd = createUpdateStatusCommandWhenErrorRaise(pendingPaymentEvent, pspException.getPaymentStatus(), pspException.getErrrMsg());
		} else if (throwable instanceof java.util.concurrent.TimeoutException) {
			log.warn("TimeoutException for checkout ID: {}", pendingPaymentEvent.getCheckoutId());
			updateCmd = createUpdateStatusCommandWhenErrorRaise(pendingPaymentEvent, PaymentStatus.EXECUTING, "TimeoutException");
		} else {
			log.warn("Unknown error for checkout ID: {}. Error: {}", pendingPaymentEvent.getCheckoutId(), throwable.getMessage());
			updateCmd = createUpdateStatusCommandWhenErrorRaise(pendingPaymentEvent, PaymentStatus.UNKNOWN, throwable.getMessage());
		}

		if(pendingPaymentEvent.getFailCount() == pendingPaymentEvent.getThreshold()){
			log.warn("MAX retry for checkout ID: {}. Error: {}", pendingPaymentEvent.getCheckoutId(), throwable.getMessage());
			updateCmd = createUpdateStatusCommandWhenErrorRaise(pendingPaymentEvent, PaymentStatus.FAILURE, "MAX RETRY");
		}

		return paymentStatusUpdatePort.updatePaymentStatus(updateCmd)
			.doOnError(error -> log.error("Error during payment status update: {}", error.getMessage()))
			.then(Mono.error(throwable));
	}

	private Mono<PaymentExecutionResult> executePendingOrder(PendingPaymentEvent pendingPaymentEvent) {
		log.info("Executing payment order for PaymentKey: {}, CheckoutId: {}", pendingPaymentEvent.getPaymentKey(), pendingPaymentEvent.getCheckoutId());
		PaymentConfirmResult paymentConfirmResult = new PaymentConfirmResult(pendingPaymentEvent.getPaymentKey(), pendingPaymentEvent.getCheckoutId(), pendingPaymentEvent.getTotalAmount());
		return paymentExecutionPort.execute(paymentConfirmResult)
			.doOnNext(result -> log.info("Payment execution result for PaymentKey: {}: {}", pendingPaymentEvent.getPaymentKey(), result));
	}

	public PaymentStatusUpdateCommand createUpdateStatusCommandWhenErrorRaise(PendingPaymentEvent pendingPaymentEvent, PaymentStatus status, String reason) {
		log.info("Creating PaymentStatusUpdateCommand for CheckoutId: {}, Status: {}, Reason: {}", pendingPaymentEvent.getCheckoutId(), status, reason);
		return new PaymentStatusUpdateCommand(pendingPaymentEvent.getCheckoutId(), status, LocalDateTime.now(), reason, pendingPaymentEvent.getFailCount());
	}

	private Mono<PaymentExecutionResult> finalizePaymentStatus(PaymentExecutionResult executionResult) {
		log.info("Finalizing payment status for CheckoutId: {}", executionResult.getCheckoutId());
		PaymentStatusUpdateCommand updateCommand = new PaymentStatusUpdateCommand(executionResult.getCheckoutId(), executionResult.getStatus(), executionResult.getApprovedAt(), "Confirm 응답 기록");
		return paymentStatusUpdatePort.updatePaymentStatus(updateCommand)
			.thenReturn(executionResult);
	}
}
