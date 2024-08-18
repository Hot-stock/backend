package com.bjcareer.stockservice.timeDeal.repository.coupon;


import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCouponRepositoryImpl implements CustomCouponRepository {
	@PersistenceContext
	private final EntityManager em;

	@Override
	public void saveAsync(Coupon coupon) {

	}
}
