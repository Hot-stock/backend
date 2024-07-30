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


    public boolean tryLock(String lockName) {
        log.debug("lock Name {} 획득 요청", lockName);
        RLock lock = redissonClient.getLock(lockName);
        boolean result = false;

        try {
            result = lock.tryLock(1, 1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new IllegalStateException("Lock 요청 실패");
        }

        log.debug("lock Name {} 획득 결과 {}", lockName, result);
        return result;
    }

    public void releaselock(String lockName) {
        log.debug("lock release {}", lockName);
        RLock lock = redissonClient.getLock(lockName);
        lock.unlock();
    }


}
