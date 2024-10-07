package com.bjcareer.gateway.aop.ports.out;

import java.util.Optional;

import com.bjcareer.gateway.domain.TokenBucket;

public interface RateLimitPort {
	String BUCKET_KEY = "RATE_LIMIT_BUCKET:";
	Integer expirationSec = 24 * 3600;

	Optional<TokenBucket> loadTokenBucket(String key);
	void saveTokenBucket(String key, TokenBucket tokenBucket);
}
