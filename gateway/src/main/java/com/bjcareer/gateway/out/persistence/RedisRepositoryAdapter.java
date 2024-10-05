package com.bjcareer.gateway.out.persistence;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.gateway.aop.ports.out.RateLimitPort;
import com.bjcareer.gateway.domain.TokenBucket;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryAdapter implements RateLimitPort {
	private final RedissonClient redissonClient;

	@Override
	public Optional<TokenBucket> loadTokenBucket(String key) {
		String buketKey = RateLimitPort.BUCKET_KEY + key;
		RBucket<TokenBucket> bucket = redissonClient.getBucket(buketKey);
		return Optional.ofNullable(bucket.get());
	}

	@Override
	public void saveTokenBucket(String key, TokenBucket tokenBucket) {
		String buketKey = RateLimitPort.BUCKET_KEY + key;
		RBucket<TokenBucket> bucket = redissonClient.getBucket(buketKey);
		bucket.set(tokenBucket, Duration.of(expirationSec, TimeUnit.SECONDS.toChronoUnit()));
	}
}
