package com.bjcareer.payment.payment.adapter.application.port.domain.entity.order;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("payment_order")
@Getter @Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentOrder {

	public static final int MAX_RETRY_COUNT = 5;
	@Id
	@Column("payment_order_id")
	private Long id;

	@Column("payment_event_id")
	private Long paymentEventId;

	@Column("product_id")
	private Long productId;

	@Column("payment_order_status")
	private PaymentStatus paymentStatus;

	@Column("ledger_update")
	private boolean ledgerUpdate;

	@Column("wallet_update")
	private boolean walletUpdate;

	@Column("fail_count")
	private int failCount;

	@Column("threshold")
	private int threshold;

	@Column("create_at")
	private LocalDateTime createAt;

	@Column("updated_at")
	private LocalDateTime updatedAt;

	public PaymentOrder(Long productId) {
		this.productId = productId;

		this.paymentStatus = PaymentStatus.NOT_STARTED;
		this.ledgerUpdate = false;
		this.walletUpdate = false;
		this.failCount = 0;
		this.threshold = MAX_RETRY_COUNT;
		this.createAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public void assignRelatedPaymentEventId(Long id){
		this.paymentEventId = id;
	}

}

