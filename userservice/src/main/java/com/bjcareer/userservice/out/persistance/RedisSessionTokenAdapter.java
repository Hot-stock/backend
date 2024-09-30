package com.bjcareer.userservice.out.persistance;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.auth.token.valueObject.TokenVO;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.RemoveTokenPort;
import com.bjcareer.userservice.application.ports.out.SaveTokenPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class RedisSessionTokenAdapter implements LoadTokenPort, SaveTokenPort, RemoveTokenPort {
	private final RedissonClient redissonClient;
	private final String OBJECT_KEY = "USER:LOGIN:";


	@Override
	public void saveJWT(String sessionId, JwtTokenVO token, Long expirationTime) {
		log.debug("Saving JWT with sessionId = {}", sessionId);
		saveToRedis(sessionId, token, expirationTime);
	}

	@Override
	public void saveAuthToken(TokenVO token, Long expirationTime) {
		log.debug("Saving AuthToken with telegramId = {}", token.getTelegramId());
		saveToRedis(token.getTelegramId(), token, expirationTime);
	}

	@Override
	public Optional<JwtTokenVO> findTokenBySessionId(String sessionId) {
		log.debug("Finding JWT with sessionId = {}", sessionId);
		return findFromRedis(sessionId);
	}

	@Override
	public boolean removeToken(String sessionId) {
		log.debug("Removing JWT with key = {}", sessionId);
		return redissonClient.getBucket(sessionId).expire(Duration.ZERO);
	}

	@Override
	public Optional<TokenVO> loadByTelemgramId(String token) {
		return Optional.empty();
	}

	private <T> void saveToRedis(String key, T value, long expirationTime) {
		key = makeKey(key);
		RBucket<Object> bucket = redissonClient.getBucket(key);
		bucket.set(value, Duration.of(expirationTime, TimeUnit.SECONDS.toChronoUnit()));
	}

	private <T> Optional<T> findFromRedis(String key) {
		key = makeKey(key);
		RBucket<T> bucket = redissonClient.getBucket(key);
		if (bucket.isExists()) {
			return Optional.of(bucket.get());
		}

		return Optional.empty();
	}


	private String makeKey(String target) {
		return OBJECT_KEY + target;
	}
}
