package com.bjcareer.userservice.repository;

import com.bjcareer.userservice.service.vo.TokenVO;
import com.bjcareer.userservice.domain.User;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedissonClient redissonClient;

    public void saveUser(User user, int keepAlive) {
        RBucket<Object> bucket = redissonClient.getBucket(user.getUserId());
        bucket.set(user, Duration.of(keepAlive, TimeUnit.SECONDS.toChronoUnit()));
    }

    public Optional<User> findUserById(String userId){
        RBucket<Object> bucket = redissonClient.getBucket(userId);
        if (bucket.isExists()) {
            return Optional.of((User) bucket.get());
        }
        return Optional.empty();
    }

    public void saveToken(TokenVO token, int expirationTime) {
        RBucket<Object> bucket = redissonClient.getBucket(token.getTelegramId());
        bucket.set(token, Duration.of(expirationTime, TimeUnit.SECONDS.toChronoUnit()));
    }

    public Optional<TokenVO> findTokebByTelegramId(String userId){
        RBucket<Object> bucket = redissonClient.getBucket(userId);
        if (bucket.isExists()) {
            return Optional.of((TokenVO) bucket.get());
        }
        return Optional.empty();
    }

}
