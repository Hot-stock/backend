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
	public static final long EXPIRATION_SEC = 500L;
	private final RedissonClient redissonClient;
	private final String OBJECT_LOGIN_KEY = "USER:LOGIN:";
	private final String OBJECT_REGISTER_KEY = "USER:REGISTER:";
	private final String OBJECT_VERIFY_USER_KEY = "USER:VERIFY:";


	@Override
	public void saveJWT(String sessionId, JwtTokenVO token, Long expirationTime) {
		log.debug("Saving JWT with sessionId = {}", sessionId);
		saveToRedis(OBJECT_LOGIN_KEY + sessionId, token, expirationTime);
	}

	@Override
	public Optional<JwtTokenVO> findTokenBySessionId(String sessionId) {
		log.debug("Finding JWT with sessionId = {}", sessionId);
		return findFromRedis(OBJECT_LOGIN_KEY + sessionId);
	}

	@Override
	public boolean removeToken(String sessionId) {
		log.debug("Removing JWT with key = {}", sessionId);
		return redissonClient.getBucket(OBJECT_LOGIN_KEY + sessionId).expire(Duration.ZERO);
	}

	@Override
	public void saveVerificationToken(TokenVO tokenVO) {
		saveToRedis(OBJECT_REGISTER_KEY + tokenVO.getEmail(), tokenVO, EXPIRATION_SEC);
	}

	@Override
	public Optional<TokenVO> loadVerificationTokenByEmail(String email) {
		return findFromRedis(OBJECT_REGISTER_KEY + email);
	}

	@Override
	public void saveVerifiedUser(TokenVO tokenVO) {
		saveToRedis(OBJECT_VERIFY_USER_KEY + tokenVO.getEmail(), tokenVO, EXPIRATION_SEC);
	}

	@Override
	public Optional<TokenVO> loadVerifiedUserByEmail(String email) {
		return findFromRedis(OBJECT_VERIFY_USER_KEY + email);
	}

	private <T> void saveToRedis(String key, T value, long expirationSec) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		bucket.set(value, Duration.of(expirationSec, TimeUnit.SECONDS.toChronoUnit()));
	}

	private <T> Optional<T> findFromRedis(String key) {
		RBucket<T> bucket = redissonClient.getBucket(key);
		if (bucket.isExists()) {
			return Optional.of(bucket.get());
		}

		return Optional.empty();
	}

}
