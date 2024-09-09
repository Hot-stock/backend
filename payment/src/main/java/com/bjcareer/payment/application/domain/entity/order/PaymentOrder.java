package com.bjcareer.payment.application.domain.entity.order;
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

	@Column("amount")
	private int amount;

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

	public PaymentOrder(Long productId, int amount) {
		this.productId = productId;
		this.amount = amount;

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

	public void executePayment(){
		if (isFinished()) {
			return;
		}

		this.paymentStatus = PaymentStatus.EXECUTING;
	}

	public void executeSuccess(){
		this.paymentStatus = PaymentStatus.SUCCESS;
	}

	public void executeFailure(){
		this.paymentStatus = PaymentStatus.FAILURE;
	}

	public void executeUnknown(){
		this.paymentStatus = PaymentStatus.UNKNOWN;
	}

	public void updateApprovedAt(LocalDateTime approvedAt){
		this.updatedAt = approvedAt;
	}

	private boolean isFinished(){
		if (this.paymentStatus == PaymentStatus.SUCCESS || this.paymentStatus == PaymentStatus.FAILURE) {
			return true;
		}

		return false;
	}

}

