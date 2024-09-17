package com.bjcareer.payment.adapter.out.persistent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import com.bjcareer.payment.adapter.out.persistent.exceptions.DataNotFoundException;
import com.bjcareer.payment.adapter.out.persistent.repository.command.PaymentEventCommand;
import com.bjcareer.payment.adapter.out.persistent.repository.command.PaymentOrderCommand;
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
				BiConsumer<PaymentEvent, PaymentStatusUpdateCommand> paymentEventConsumer = PaymentEventCommand.commands.get(
					command.getStatus());
				paymentEventConsumer.accept(paymentEvent, command);
				return paymentRepository.save(paymentEvent).thenReturn(true);
			});
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
				Consumer<PaymentOrder> paymentOrderConsumer = PaymentOrderCommand.command.get(command.getStatus());
				paymentOrderConsumer.accept(paymentOrder);
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
