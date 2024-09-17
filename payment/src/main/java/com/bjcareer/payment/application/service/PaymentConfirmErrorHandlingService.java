package com.bjcareer.payment.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import com.bjcareer.payment.adapter.out.web.psp.exceptions.PspConfirmationException;
import com.bjcareer.payment.application.domain.PaymentStatusUpdateCommand;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;
import com.bjcareer.payment.application.port.in.PaymentConfirmCommand;

@Component
public class PaymentConfirmErrorHandlingService {

	public PaymentStatusUpdateCommand handle(PspConfirmationException pspConfirmationException, PaymentConfirmCommand command) {
		return createUpdateStatusCommandWhenErrorRaise(command.getCheckoutId(), pspConfirmationException.getPaymentStatus(), pspConfirmationException.getErrrMsg());
	}

	public PaymentStatusUpdateCommand createUpdateStatusCommandWhenErrorRaise(String checkoutId, PaymentStatus status, String reason) {
		return new PaymentStatusUpdateCommand(checkoutId, status, LocalDateTime.now(), reason);
	}
}
