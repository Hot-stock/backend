package com.bjcareer.payment.payment.adapter.application.port.out;

import java.util.List;

import com.bjcareer.payment.payment.adapter.application.port.domain.entity.coupon.PaymentCoupon;

public interface LoadCouponPort {
	List<PaymentCoupon> getCoupons(List<Long> couponIds);
}
