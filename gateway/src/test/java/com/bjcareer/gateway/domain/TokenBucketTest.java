package com.bjcareer.gateway.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bjcareer.gateway.exceptions.TooManyRequestsException;

class TokenBucketTest {
	private TokenBucket tokenBucket;
	private Clock clock;

	@BeforeEach
	void setUp() {
		clock = mock(Clock.class);  // Clock 모킹
		Instant initialInstant = Instant.now();

		when(clock.instant()).thenReturn(initialInstant);  // 초기 시간을 설정
		when(clock.getZone()).thenReturn(ZoneId.systemDefault());

		tokenBucket = new TokenBucket();  // 모킹된 Clock을 사용하여 TokenBucket 초기화
	}

	@Test
	void 토큰을_초과해서_사용하면_assert_raise() {
		for (int i = 0; i < TokenBucket.MAX_REQUESTS; i++) {
			tokenBucket.attemptApiCall();
		}

		assertThrows(TooManyRequestsException.class, () -> {
			tokenBucket.attemptApiCall();
		});
	}

	@Test
	void 토큰이_정상적으로_감소하는지() {
		tokenBucket.attemptApiCall();
		assertEquals(TokenBucket.MAX_REQUESTS - 1, tokenBucket.getAvailableTokens());
	}

	@Test
	void 토큰_리필_가능여부() {
		tokenBucket.attemptApiCall();  // 호출 시 토큰이 리필됨
		Instant tenMinute = Instant.now().plusSeconds(TokenBucket.REFILL_INTERVAL_SECONDS );
		when(clock.instant()).thenReturn(tenMinute);

		tokenBucket.attemptApiCall();  // 호출 시 토큰이 리필됨
		assertEquals(TokenBucket.MAX_REQUESTS - 1, tokenBucket.getAvailableTokens());
	}
}
