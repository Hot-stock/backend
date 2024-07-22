package com.bjcareer.stockservice.timeDeal.repository;

import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import org.apache.commons.lang3.ClassUtils;

public interface CouponRepository {
    public String save(Coupon coupon);
    public Coupon findById(String id);
}
