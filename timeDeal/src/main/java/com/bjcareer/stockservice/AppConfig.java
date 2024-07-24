package com.bjcareer.stockservice;

import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.coupon.UnlockCouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.event.RedisTimeDealRepository;
import com.bjcareer.stockservice.timeDeal.repository.event.PessimiticLockEventRepository;


import jakarta.persistence.EntityManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();

        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://localhost:6379");
        singleServerConfig.setPassword("timo");
        singleServerConfig.setDatabase(0);

        return Redisson.create(config);
    }

    @Bean
    public RedisTimeDealRepository inmemoryEventRepository(RedissonClient redissonClient) {
        return new RedisTimeDealRepository(redissonClient);
    }
}
