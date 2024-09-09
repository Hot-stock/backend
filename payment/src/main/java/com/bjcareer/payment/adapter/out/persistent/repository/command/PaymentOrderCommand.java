package com.bjcareer.payment.adapter.out.persistent.repository.command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;

public class PaymentOrderCommand {
	public static final Map<PaymentStatus, Consumer<PaymentOrder>> command = new HashMap<>();

	static {
		command.put(PaymentStatus.SUCCESS, PaymentOrder::executeSuccess);
		command.put(PaymentStatus.FAILURE, PaymentOrder::executeFailure);
		command.put(PaymentStatus.UNKNOWN, PaymentOrder::executeUnknown);
		command.put(PaymentStatus.EXECUTING, PaymentOrder::executePayment);
	}
}
