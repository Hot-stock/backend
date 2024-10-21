package com.bjcareer.stockservice.timeDeal.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bjcareer.stockservice.timeDeal.application.ports.out.OutboxCouponPort;
import com.bjcareer.stockservice.timeDeal.domain.coupon.OutboxCoupon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class OutBoxCouponPortAdapter implements OutboxCouponPort {
	private final OutBoxCouponRepository outBoxCouponRepository;

	@Override
	public List<OutboxCoupon> load() {
		return outBoxCouponRepository.findIsNotDelivered();
	}

	@Override
	public void saveAll(List<OutboxCoupon> outboxCoupons) {
		outBoxCouponRepository.saveAll(outboxCoupons);
	}
}
