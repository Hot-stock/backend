package com.bjcareer.stockservice.timeDeal.repository;

import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface CouponRepository {
    public String save(Coupon coupon);
    public Coupon findById(String id);
    @Async
    @Transactional
    public void saveAsync(Coupon coupon);
}
