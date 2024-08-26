package com.bjcareer.payment.payment.adapter.application.port.domain.entity.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("payment_order_history")
public class PaymentOrderHistory {

	@Id
	@Column("id")
	private Long id;

	@Column("payment_order_id")
	private Long paymentOrderId;

	@Column("previous_status")
	private PaymentStatus previousStatus;

	@Column("new_status")
	private PaymentStatus newStatus;

	@Column("created_at")
	private LocalDateTime createdAt;

	@Column("changed_by")
	private String changedBy;

	@Column("reason")
	private String reason;
}
