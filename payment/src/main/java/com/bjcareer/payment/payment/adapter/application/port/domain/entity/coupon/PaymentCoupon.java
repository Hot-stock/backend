package com.bjcareer.payment.payment.adapter.application.port.domain.entity.coupon;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Table
@NoArgsConstructor
@Getter
public class PaymentCoupon {
	@Id
	@Column("coupon_id")
	private Long id;

	@Column("coupon_id")
	private Long couponId;

	@Column("payment_event_id")
	private Long paymentEventId;

	@Column("percentage")
	private int percentage;

	public PaymentCoupon(Long couponId, int percentage) {
		this.couponId = couponId;
		this.percentage = percentage;
	}


	public void assignRelatedPaymentEventId(Long id){
		this.paymentEventId = id;
	}
}
