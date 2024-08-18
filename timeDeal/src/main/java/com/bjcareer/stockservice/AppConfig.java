package com.bjcareer.stockservice;

import com.bjcareer.stockservice.timeDeal.repository.event.RedisTimeDealRepository;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();

        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://localhost:6379");
        singleServerConfig.setPassword("changeme");
        singleServerConfig.setDatabase(0);

        return Redisson.create(config);
    }

    @Bean
    public RedisTimeDealRepository inmemoryEventRepository(RedissonClient redissonClient) {
        return new RedisTimeDealRepository(redissonClient);
    }
}
