package com.bjcareer.payment.application.domain.entity.event;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.domain.entity.order.PaymentOrder;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("payment_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
@Data
public class PaymentEvent {

	@Id
	@Column("payment_event_id")
	private Long id;

	@Column("buyer_id")
	private String buyerId;

	@Column("order_id")
	private String orderId; //checkout_id uuid를 사용한다고 가정

	@Column("payment_key")
	private String paymentKey;

	@Column("payment_method")
	private PaymentMethod paymentMethod;

	@Column("is_payment_done")
	private boolean isPaymentDone;

	@Column("created_at")
	private LocalDateTime createAt;

	@Column("updated_at")
	private LocalDateTime updatedAt;

	@Transient
	private List<PaymentOrder> orders = new ArrayList<>();

	@Transient
	private List<PaymentCoupon> coupons = new ArrayList<>();


	public PaymentEvent(String buyerId, String orderId, List<PaymentOrder> orders, List<PaymentCoupon> coupon) {
		this(null, buyerId, orderId, orders, coupon);
	}

	public PaymentEvent(Long Id, String buyerId, String orderId, List<PaymentOrder> orders, List<PaymentCoupon> coupon) {
		this(Id, buyerId, orderId, null, orders, coupon);
	}
	public PaymentEvent(Long Id, String buyerId, String orderId, String paymentKey, List<PaymentOrder> orders, List<PaymentCoupon> coupon) {
		this.id = Id;
		this.paymentKey = paymentKey;
		initVariable(buyerId, orderId, orders, coupon);
	}


	public Long getTotalAmount(){
		return orders.stream().mapToLong(PaymentOrder::getAmount).sum();
	}

	public void updatePaymentKey(String paymentKey){
		this.paymentKey = paymentKey;
	}

	private void initVariable(String buyerId, String orderId, List<PaymentOrder> orders, List<PaymentCoupon> coupon) {
		this.buyerId = buyerId;
		this.orderId = orderId;
		this.orders = orders;
		this.coupons = coupon;

		this.createAt = LocalDateTime.now();
		this.isPaymentDone = false;
		this.updatedAt = LocalDateTime.now();
		this.paymentMethod = PaymentMethod.CREDIT_CARD;
	}

	public void setPaymnetFinish(){
		this.isPaymentDone = true;
	}

}