package com.bjcareer.payment.adapter.out.web.coupon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.application.port.out.LoadCouponPort;

@Component
public class CouponWebAdapter implements LoadCouponPort {
	public static final int TEMP_PERCENTAGE = 20;

	public List<PaymentCoupon> getCoupons(List<Long> couponIds){
		List<PaymentCoupon> coupons = new ArrayList<>();
		for (Long couponId : couponIds) {
			PaymentCoupon coupon = new PaymentCoupon(couponId, TEMP_PERCENTAGE);
			coupons.add(coupon);
		}

		return coupons;
	}
}
