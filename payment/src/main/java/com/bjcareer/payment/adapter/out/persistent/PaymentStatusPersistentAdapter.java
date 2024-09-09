package com.bjcareer.payment.adapter.out.persistent;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import com.bjcareer.payment.adapter.out.persistent.exceptions.DataNotFoundException;
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
	private final PaymentEventRepository paymentRepository;
	private final PaymentOrderRepository paymentOrderRepository;
	private final PaymentOrderHistoryRepository paymentOrderHistoryRepository;
	private final TransactionalOperator transactionalOperator;


	@Override
	public Mono<Boolean> updatePaymentStatus(PaymentStatusUpdateCommand command) {
		return updatePaymentStatusWithCommand(command)
			.flatMap(paymentEvent -> {
				if (command.getStatus() == PaymentStatus.SUCCESS || command.getStatus() == PaymentStatus.FAILURE) {
					paymentEvent.setPaymentFinished();
					return paymentRepository.save(paymentEvent)
						.thenReturn(true);
				}else if(command.getStatus() == PaymentStatus.NOT_STARTED){
					paymentEvent.updatePaymentKey(command.getPaymentKey());
					return paymentRepository.save(paymentEvent).thenReturn(true);
				}
				return Mono.just(true);
			});
	}

	private Mono<PaymentEvent> updatePaymentKeyAndProcessOrders(PaymentEvent paymentEvent, PaymentStatusUpdateCommand command, String paymentKey) {
		paymentEvent.updatePaymentKey(paymentKey);
		return findAndProcessPaymentOrders(paymentEvent, command)
			.then(Mono.just(paymentEvent));
	}

	private Flux<PaymentOrder> findAndProcessPaymentOrders(PaymentEvent paymentEvent, PaymentStatusUpdateCommand command) {
		return findPaymentOrdersByEvent(paymentEvent)
			.flatMap(it ->
				updatePaymentOrderStatus(it, command)
					.flatMap(paymentOrderRepository::save));
	}

	private Mono<PaymentEvent> updatePaymentStatusWithCommand(PaymentStatusUpdateCommand command) {
		return transactionalOperator.transactional(
			paymentRepository.findByCheckoutId(command.getCheckoutId())
				.flatMapMany(this::findPaymentOrdersByEvent)
				.flatMap(order -> {
						updatePaymentOrderDetails(command, order);
						return updatePaymentOrderStatus(order, command)
							.flatMap(paymentOrderRepository::save);
						}))
			.then(paymentRepository.findByCheckoutId(command.getCheckoutId()));
	}

	private Flux<PaymentOrder> findPaymentOrdersByEvent(PaymentEvent paymentEvent) {
		return paymentOrderRepository.findByPaymentEventId(paymentEvent.getId());
	}

	private Mono<PaymentOrder> updatePaymentOrderStatus(PaymentOrder paymentOrder, PaymentStatusUpdateCommand command) {
		return updatePaymentHistory(paymentOrder, command)
			.flatMap(it ->
			{
				if (command.getStatus() == PaymentStatus.SUCCESS) {
					paymentOrder.executeSuccess();
				} else if (command.getStatus() == PaymentStatus.FAILURE) {
					paymentOrder.executeFailure();
				} else if (command.getStatus() == PaymentStatus.EXECUTING) {
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

	private Mono<PaymentOrderHistory> updatePaymentHistory(PaymentOrder paymentOrder, PaymentStatusUpdateCommand command) {
		return paymentOrderHistoryRepository.findByOrderId(paymentOrder.getId())
			.switchIfEmpty(createNewPaymentOrderHistory(paymentOrder, command))
			.flatMap(paymentOrderHistory -> {
				paymentOrderHistory.changeStatus(command.getStatus(), command.getReason(), command.getChangedBy());
				return paymentOrderHistoryRepository.save(paymentOrderHistory);
			});
	}

	private Mono<PaymentOrderHistory> createNewPaymentOrderHistory(PaymentOrder paymentOrder, PaymentStatusUpdateCommand command) {
		return paymentOrderHistoryRepository.save(
			new PaymentOrderHistory(paymentOrder.getId(), command.getStatus().toString())
		);
	}
}
