package com.bjcareer.payment.application.domain.entity.event;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("payment_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class PaymentEvent {

	@Id
	@Column("payment_event_id")
	private Long id;

	@Column("buyer_id")
	private String buyerId;

	@Column("checkout_id")
	private String checkoutId; // checkout_id uuid를 사용

	@Column("payment_key")
	private String paymentKey;

	@Column("payment_method")
	private PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD; // 기본값 설정

	@Column("is_payment_done")
	private boolean isPaymentDone = false; // 기본값 설정

	@Column("created_at")
	private LocalDateTime createdAt;

	@Column("updated_at")
	private LocalDateTime updatedAt;

	@Transient
	private List<PaymentOrder> orders = new ArrayList<>();

	@Transient
	private List<PaymentCoupon> coupons = new ArrayList<>();

	public PaymentEvent(String buyerId, String checkoutId, List<PaymentOrder> orders, List<PaymentCoupon> coupons) {
		this.buyerId = buyerId;
		this.checkoutId = checkoutId;
		this.orders = orders;
		this.coupons = coupons;

		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public PaymentEvent(PaymentEvent paymentEvent, List<PaymentOrder> orders, List<PaymentCoupon> coupons) {
		this(paymentEvent.getBuyerId(), paymentEvent.getCheckoutId(), orders, coupons);
		this.id = paymentEvent.getId();
		this.isPaymentDone = paymentEvent.isPaymentDone();
		this.createdAt = paymentEvent.getCreatedAt();
		this.updatedAt = paymentEvent.getUpdatedAt();
		this.paymentMethod = paymentEvent.getPaymentMethod();
	}

	public Long getTotalAmount() {
		return orders.stream().mapToLong(PaymentOrder::getAmount).sum();
	}

	public void updatePaymentKey(String paymentKey) {
		this.paymentKey = paymentKey;
	}

	public void setPaymentFinished() {
		this.isPaymentDone = true;
		this.updatedAt = LocalDateTime.now(); // 결제 완료 시점으로 업데이트
	}
}
