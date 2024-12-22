package com.bjcareer.search.out.persistence.cache;

import java.util.List;
import java.util.Set;

import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.bjcareer.search.domain.TreeMapDomain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisTreeMapAdapter {
	private final RedissonClient redissonClient;
	private final static String BUKET_KEY = "MARKET_MAP";

	public void uploadTreemap(List<TreeMapDomain> domains) {
		domains.forEach(domain -> {
			// Redis 버킷 키 생성
			RSet<TreeMapDomain> bucket = redissonClient.getSet(BUKET_KEY);
			bucket.add(domain);

		});
	}

	public List<TreeMapDomain> getTreemap() {
		RSet<TreeMapDomain> set = redissonClient.getSet(BUKET_KEY);
		Set<TreeMapDomain> objects = set.readAll();
		return objects.stream().toList();
	}
}
