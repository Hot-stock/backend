package com.bjcareer.search.out.persistence.cache;

import java.time.Duration;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RedisS3Adapter {
	private final RedissonClient redissonClient;
	private final static String BUKET_KEY = "LOGO:STOCK:";

	public Pair<Boolean, String> getLogo(String code) {
		String key = BUKET_KEY + code;
		RBucket<String> bucket = redissonClient.getBucket(key);

		if (bucket.isExists()) {
			return Pair.of(true, bucket.get());
		}

		return Pair.of(false, "");
	}

	public void updateLogo(String code, String url, Duration duration) {
		String key = BUKET_KEY + code;
		RBucket<String> bucket = redissonClient.getBucket(key);
		bucket.set(url, duration);
	}
}
