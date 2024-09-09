package com.bjcareer.payment.application.domain;

import java.time.LocalDateTime;

import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;

import lombok.Data;

@Data
public class PaymentStatusUpdateCommand {
	private final String orderId;
	private final PaymentStatus status;
	private final LocalDateTime approvedAt;
}
