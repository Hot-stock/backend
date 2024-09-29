package com.bjcareer.userservice.repository;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.userservice.application.token.valueObject.TokenVO;
import com.bjcareer.userservice.domain.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisRepository {

    private final RedissonClient redissonClient;

    public void saveUser(User user, int keepAlive) {
        saveToRedis(user.getAlais(), user, keepAlive);
    }

    public Optional<User> findUserById(String userId) {
        return findFromRedis(userId, User.class);
    }

    public void saveToken(TokenVO token, Long expirationTime) {
        saveToRedis(token.getTelegramId(), token, expirationTime);
    }

    public Optional<TokenVO> findTokenByTelegramId(String telegramId) {
        return findFromRedis(telegramId, TokenVO.class);
    }

    private <T> void saveToRedis(String key, T value, long expirationTime) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value, Duration.of(expirationTime, TimeUnit.SECONDS.toChronoUnit()));
    }

    private <T> Optional<T> findFromRedis(String key, Class<T> type) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (bucket.isExists()) {
            return Optional.of(type.cast(bucket.get()));
        }
        return Optional.empty();
    }
}
