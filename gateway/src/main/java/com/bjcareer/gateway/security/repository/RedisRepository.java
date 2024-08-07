package com.bjcareer.gateway.security.repository;

import com.bjcareer.gateway.security.vo.JwtTokenVO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedissonClient redissonClient;


    public Optional<JwtTokenVO> findAuthTokenBySessionId(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (bucket.isExists()) {
            return Optional.of((JwtTokenVO) bucket.get());
        }
        return Optional.empty();
    }

}
