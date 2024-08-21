package com.bjcareer.stockservice.timeDeal.domain.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class Redis {

	private final RedissonClient client;
	private static final long WAIT_TIME = 1L;
	private static final long LEASE_TIME = 1L;
	Map<String, RScoredSortedSet<?>> cachePQ = new ConcurrentHashMap<>();

	public boolean acquireLock(String key) throws InterruptedException {
		log.debug("Requesting lock acquisition for key: {}", key);
		RLock lock = client.getLock(key);
		boolean acquired = false;
		acquired = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.MINUTES);
		log.debug("Lock acquisition result for key {}: {}", key, acquired);
		return acquired;
	}

	public void releaseLock(String key) {
		log.debug("Releasing lock for key: {}", key);
		RLock lock = client.getLock(key);
		if (lock.isHeldByCurrentThread()) {
			lock.unlock();
		} else {
			log.warn("Attempt to release a lock not held by the current thread: {}", key);
		}
	}

	public String getSingleKeyUsingScan(String keyPattern) {
		log.debug("Retrieving single key for pattern: {}", keyPattern);
		RKeys keys = client.getKeys();
		Iterable<String> matchingKeys = keys.getKeysByPattern(keyPattern, 1);
		return matchingKeys.iterator().hasNext() ? matchingKeys.iterator().next() : null;
	}

	public <T> RScoredSortedSet<T> getScoredSortedSet(String name) {
		RScoredSortedSet<T> pq = (RScoredSortedSet<T>) cachePQ.get(name);

		if (pq == null) {
			cachePQ.put(name, client.getScoredSortedSet(name));
		}

		return (RScoredSortedSet<T>)cachePQ.get(name);
	}

	public void removeCacheScoredSortedSet(String key) {
		cachePQ.remove(key);
	}
}
