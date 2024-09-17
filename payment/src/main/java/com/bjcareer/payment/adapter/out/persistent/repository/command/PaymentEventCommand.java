package com.bjcareer.payment.adapter.out.persistent.repository.command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;
import com.bjcareer.payment.application.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;

public class PaymentEventCommand {
	public static final Map<PaymentStatus, BiConsumer<PaymentEvent, PaymentStatusUpdateCommand>> commands = new HashMap<>();

	static {
		commands.put(PaymentStatus.SUCCESS, (paymentEvent, command) -> paymentEvent.setPaymentFinished());
		commands.put(PaymentStatus.FAILURE, (paymentEvent, command) -> paymentEvent.setPaymentFinished());
		commands.put(PaymentStatus.NOT_STARTED, (paymentEvent, command) -> {
			paymentEvent.updatePaymentKey(command.getPaymentKey());
		});
	}
}
