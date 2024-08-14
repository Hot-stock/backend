package com.bjcareer.userservice.repository;

import com.bjcareer.userservice.service.vo.JwtTokenVO;
import com.bjcareer.userservice.service.vo.TokenVO;
import com.bjcareer.userservice.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisRepository {

    private final RedissonClient redissonClient;

    public void saveUser(User user, int keepAlive) {
        saveToRedis(user.getUserId(), user, keepAlive);
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

    public void saveJWT(String key, JwtTokenVO token, Long expirationTime) {
        log.debug("Saving JWT with key = {}", key);
        saveToRedis(key, token, expirationTime);
    }

    public Optional<JwtTokenVO> findAuthTokenBySessionId(String key) {
        log.debug("Finding JWT with key = {}", key);
        return findFromRedis(key, JwtTokenVO.class);
    }

    public boolean removeJWT(String key) {
        log.debug("Removing JWT with key = {}", key);
        return redissonClient.getBucket(key).expire(Duration.ZERO);
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
