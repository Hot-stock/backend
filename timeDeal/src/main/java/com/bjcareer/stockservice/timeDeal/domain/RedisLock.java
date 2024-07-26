package com.bjcareer.stockservice.timeDeal.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisLock {
    private final RedissonClient redissonClient;
    private final Long WAIT_TIME = 1L;
    private final Long LEASE_TIME = 0L;

    public boolean tryLock(String key) {
        log.debug("lock Name {} 획득 요청", key);
        RLock lock = redissonClient.getLock(key);
        boolean result = false;

        try {
            result = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        log.debug("lock Name {} 획득 결과 {}", key, result);
        return result;
    }

    public void releaselock(String key) {
        log.debug("lock release {}", key);
        RLock lock = redissonClient.getLock(key);
        lock.unlock();
    }


}
