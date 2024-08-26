package com.bjcareer.stockservice.timeDeal.domain.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisTest {

	@Mock
	private RedissonClient redissonClient;

	@Mock
	private RLock rLock;

	@Mock
	private RKeys rKeys;

	private Redis redis;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		redis = new Redis(redissonClient);
	}

	@Test
	void testAcquireLockSuccessfully() throws InterruptedException {
		when(redissonClient.getLock("testKey")).thenReturn(rLock);
		when(rLock.tryLock(anyLong(),anyLong(), any())).thenReturn(true);

		boolean result = redis.acquireLock("testKey");

		assertTrue(result);
	}

	@Test
	void testAcquireLockFailure() throws InterruptedException {
		when(redissonClient.getLock("testKey")).thenReturn(rLock);
		when(rLock.tryLock(anyLong(),anyLong(), any())).thenReturn(false);

		boolean result = redis.acquireLock("testKey");

		assertFalse(result);
	}

	@Test
	void testAcquireLockInterruptedException() throws InterruptedException {
		when(redissonClient.getLock("testKey")).thenReturn(rLock);
		when(rLock.tryLock(anyLong(),anyLong(), any())).thenThrow(new InterruptedException());

		boolean result = redis.acquireLock("testKey");

		assertFalse(result);
	}

	@Test
	void testReleaseLockSuccessfully() {
		when(redissonClient.getLock("testKey")).thenReturn(rLock);
		when(rLock.isHeldByCurrentThread()).thenReturn(true);

		redis.releaseLock("testKey");

		verify(rLock).unlock();
	}

	@Test
	void testReleaseLockNotHeldByCurrentThread() {
		when(redissonClient.getLock("testKey")).thenReturn(rLock);
		when(rLock.isHeldByCurrentThread()).thenReturn(false);

		redis.releaseLock("testKey");

		verify(rLock, never()).unlock();
	}

	@Test
	void testGetSingleKeyFound() {
		when(redissonClient.getKeys()).thenReturn(rKeys);

		Iterator<String> iterator = mock(Iterator.class);
		when(iterator.hasNext()).thenReturn(true);
		when(iterator.next()).thenReturn("foundKey");

		when(rKeys.getKeysByPattern("testPattern", 1)).thenReturn(Collections.singletonList("foundKey"));

		String result = redis.getSingleKeyUsingScan("testPattern");

		assertEquals("foundKey", result);
	}

	@Test
	void testGetSingleKeyNotFound() {
		when(redissonClient.getKeys()).thenReturn(rKeys);
		when(rKeys.getKeysByPattern("testPattern", 1)).thenReturn(Collections.emptyList());

		String result = redis.getSingleKeyUsingScan("testPattern");

		assertNull(result);
	}
}
