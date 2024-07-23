package com.bjcareer.stockservice;

import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.coupon.UnlockCouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.event.PessimiticLockEventRepository;
import com.bjcareer.stockservice.timeDeal.repository.event.UnlockEventRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CouponRepository couponRepository(EntityManager em) {
        return new UnlockCouponRepository(em);
    }

    @Bean
    public EventRepository eventRepository(EntityManager em) {
        return new PessimiticLockEventRepository(em);
    }
}
