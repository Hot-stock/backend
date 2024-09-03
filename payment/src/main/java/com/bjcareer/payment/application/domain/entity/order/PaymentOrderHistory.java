package com.bjcareer.payment.application.domain.entity.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("payment_order_history")
public class PaymentOrderHistory {

	@Id
	@Column("payment_order_history_id")
	private Long id;

	@Column("payment_order_id")
	private Long paymentOrderId;

	@Column("previous_status")
	private PaymentStatus previousStatus;

	@Column("new_status")
	private PaymentStatus newStatus;

	@Column("created_at")
	private LocalDateTime createdAt;

	@Column("updated_at")
	private LocalDateTime updatedAt;

	@Column("changed_by")
	private String changedBy;

	@Column("reason")
	private String reason;

	public PaymentOrderHistory(Long paymentOrderId, String reason) {
		this.paymentOrderId = paymentOrderId;
		this.newStatus = PaymentStatus.NOT_STARTED;
		this.previousStatus = PaymentStatus.NOT_STARTED;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.reason = reason;
	}

	public void changeStatus(PaymentStatus newStatus, String reason){
		this.previousStatus = this.newStatus;
		this.newStatus = newStatus;
		this.reason = reason;
		this.updatedAt = LocalDateTime.now();
	}
}
