package com.bjcareer.payment.adapter.out.persistent;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrderHistory;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;
import com.bjcareer.payment.application.port.out.PaymentStatusUpdatePort;
import com.bjcareer.payment.adapter.out.persistent.repository.PaymentEventRepository;
import com.bjcareer.payment.adapter.out.persistent.repository.PaymentOrderHistoryRepository;
import com.bjcareer.payment.adapter.out.persistent.repository.PaymentOrderRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class PaymentStatusPersistentAdapter implements PaymentStatusUpdatePort {
	public static final String PAYMENT_CONFIRM_START = "PAYMENT_CONFIRM_START";
	public static final String PAYMENT_CONFIRM_SUCCESS = "PAYMENT_CONFIRM_SUCCESS";
	public static final String PAYMENT_CONFIRM_FAILURE = "PAYMENT_CONFIRM_SUCCESS";
	public static final String PAYMENT_CONFIRM_UNKNOWN = "PAYMENT_CONFIRM_SUCCESS";

	private final PaymentEventRepository paymentRepository;
	private final PaymentOrderRepository paymentOrderRepository;
	private final PaymentOrderHistoryRepository paymentOrderHistoryRepository;
	private final TransactionalOperator transactionalOperator;
	private final PaymentEventRepository paymentEventRepository;

	@Override
	public Mono<Void> updatePaymentStatusToExecuting(String checkoutId, String paymentKey) {
		return transactionalOperator.transactional(
			paymentRepository.findByCheckoutId(checkoutId)
				.flatMap(paymentEvent ->
					updatePaymentKeyAndProcessOrders(paymentEvent, paymentKey)
						.flatMap(paymentRepository::save))
				.then()
		);
	}

	@Override
	public Mono<Boolean> updatePaymentStatus(PaymentStatusUpdateCommand command) {
		if (command.getStatus() == PaymentStatus.SUCCESS){
			return updatePaymentStatus(command, PAYMENT_CONFIRM_SUCCESS);
		} else if (command.getStatus() == PaymentStatus.FAILURE) {
			return updatePaymentStatus(command, PAYMENT_CONFIRM_FAILURE);
		}else{
			return updatePaymentStatus(command, PAYMENT_CONFIRM_UNKNOWN);
		}
	}

	private Mono<PaymentEvent> updatePaymentKeyAndProcessOrders(PaymentEvent paymentEvent, String paymentKey) {
		paymentEvent.updatePaymentKey(paymentKey);

		return findAndProcessPaymentOrders(paymentEvent, PaymentStatus.EXECUTING)
			.then(Mono.just(paymentEvent));
	}


	private Flux<PaymentOrder> findAndProcessPaymentOrders(PaymentEvent paymentEvent, PaymentStatus status) {
		return findPaymentOrdersByEvent(paymentEvent)
			.flatMap(it ->
				updatePaymentOrderStatus(it, status, PAYMENT_CONFIRM_START)
					.flatMap(paymentOrderRepository::save));
	}
	
	private Flux<PaymentOrder> setUpDonePayment(PaymentEvent payment) {
		payment.setPaymentFinished();
		return paymentEventRepository.save(payment).flatMapMany(this::findPaymentOrdersByEvent);
	}


	private Mono<Boolean> updatePaymentStatus(PaymentStatusUpdateCommand command, String reason) {
		return transactionalOperator.transactional(
			paymentRepository.findByCheckoutId(command.getCheckoutId())
				.flatMapMany(this::setUpDonePayment)
				.flatMap(order -> {
						updatePaymentOrderDetails(command, order);
						return updatePaymentOrderStatus(order, command.getStatus(), reason)
							.flatMap(paymentOrderRepository::save);
					}).then(Mono.just(true)));
	}

	private Flux<PaymentOrder> findPaymentOrdersByEvent(PaymentEvent paymentEvent) {
		return paymentOrderRepository.findByPaymentEventId(paymentEvent.getId());
	}

	private Mono<PaymentOrder> updatePaymentOrderStatus(PaymentOrder paymentOrder, PaymentStatus status, String reason) {
		return updatePaymentHistory(paymentOrder, reason)
			.flatMap(it ->
			{
				if (status == PaymentStatus.SUCCESS) {
					paymentOrder.executeSuccess();
				} else if (status == PaymentStatus.FAILURE) {
					paymentOrder.executeFailure();
				} else if (status == PaymentStatus.EXECUTING) {
					paymentOrder.executePayment();
				} else {
					paymentOrder.executeUnknown();
				}
				return Mono.just(paymentOrder);
			});
	}

	private void updatePaymentOrderDetails(PaymentStatusUpdateCommand command, PaymentOrder order) {
		order.updateApprovedAt(command.getApprovedAt());
	}

	private Mono<PaymentOrderHistory> updatePaymentHistory(PaymentOrder paymentOrder, String status) {
		return paymentOrderHistoryRepository.findByOrderId(paymentOrder.getId())
			.switchIfEmpty(createNewPaymentOrderHistory(paymentOrder, status))
			.flatMap(paymentOrderHistory -> {
				paymentOrderHistory.changeStatus(PaymentStatus.EXECUTING, status);
				return paymentOrderHistoryRepository.save(paymentOrderHistory);
			});
	}

	private Mono<PaymentOrderHistory> createNewPaymentOrderHistory(PaymentOrder paymentOrder, String status) {
		return paymentOrderHistoryRepository.save(
			new PaymentOrderHistory(paymentOrder.getId(), PaymentStatus.EXECUTING, status)
		);
	}
}
