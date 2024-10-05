package com.bjcareer.gateway.out.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.gateway.domain.TokenBucket;

@SpringBootTest
class RedisRepositoryAdapterTest {
	@Autowired RedisRepositoryAdapter redisRepositoryAdapter;

	@Test
	void 없는키로_요청했을떄() {
		Optional<TokenBucket> tokenBucket = redisRepositoryAdapter.loadTokenBucket("NULL_KEY");
		assertTrue(tokenBucket.isEmpty());
	}

}
