package com.bjcareer.stockservice.timeDeal.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class RedisTest {
    @Autowired RedissonClient redissonClient;

    @Test
    void testTryLock() throws InterruptedException {
        String key = "testKey4";

        RLock lock = redissonClient.getLock(key);
        boolean firstLockResult = lock.tryLock(5, 0, TimeUnit.SECONDS);
        System.out.println("firstLockResult = ASDASDASDASD" + firstLockResult);
    }

    @Test
    void releaselock() {
    }
}