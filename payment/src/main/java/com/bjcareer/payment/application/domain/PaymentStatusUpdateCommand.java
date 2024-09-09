package com.bjcareer.payment.application.domain;

import java.time.LocalDateTime;

import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PaymentStatusUpdateCommand {
	public static final String PAYMENT_CONFIRM_START = "PAYMENT_CONFIRM_START";
	public static final String PAYMENT_CONFIRM_SUCCESS = "PAYMENT_CONFIRM_SUCCESS";
	public static final String PAYMENT_CONFIRM_FAILURE = "PAYMENT_CONFIRM_FAILURE";
	public static final String PAYMENT_CONFIRM_UNKNOWN = "PAYMENT_CONFIRM_UNKNOWN";

	private final String checkoutId;
	private final PaymentStatus status;
	private final LocalDateTime approvedAt;
	private final String changedBy;
	private final String reason;
	private String paymentKey;

	public PaymentStatusUpdateCommand(String checkoutId, PaymentStatus status, LocalDateTime approvedAt, String reason) {
		this.checkoutId = checkoutId;
		this.status = status;
		this.approvedAt = approvedAt;
		this.reason = reason;
		this.changedBy = determineChangedBy(status);
	}

	public PaymentStatusUpdateCommand(String checkoutId, PaymentStatus status, LocalDateTime approvedAt, String reason, String paymentKey) {
		this(checkoutId, status, approvedAt,reason);
		this.paymentKey = paymentKey;
	}

	private String determineChangedBy(PaymentStatus status) {
		return switch (status) {
			case EXECUTING -> PAYMENT_CONFIRM_START;
			case SUCCESS -> PAYMENT_CONFIRM_SUCCESS;
			case FAILURE -> PAYMENT_CONFIRM_FAILURE;
			case UNKNOWN -> PAYMENT_CONFIRM_UNKNOWN;
			default -> throw new IllegalArgumentException("Unexpected status: " + status);
		};
	}
}
