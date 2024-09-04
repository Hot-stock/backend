package com.bjcareer.payment.application.domain.entity.event;

import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;

import lombok.Data;

@Data
public class PendingPaymentEvent {
	private Long paymentEventId;
	private Long paymentOrderId;
	private String checkoutId;
	private PaymentStatus status;
	private String paymentKey;
	private int failCount;
	private int threshold;
	private Long totalAmount;

	public void updateFailCount() {
		this.failCount++;
	}
}