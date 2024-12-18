package com.bjcareer.search.out.persistence.cache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RBucketAsync;
import org.redisson.api.RKeys;
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
	private final static String BUKET_KEY = "TREEMAP:";

	public void uploadTreemap(List<TreeMapDomain> domains) {
		domains.forEach(domain -> {
			// Redis 버킷 키 생성
			RBucket<TreeMapDomain> bucket = redissonClient.getBucket(BUKET_KEY + domain.getThemaName());
			bucket.set(domain, Duration.ofDays(1));

		});
	}

	public List<TreeMapDomain> getTreemap() {
		String key = BUKET_KEY + "*"; // Redis 키 패턴
		List<String> keys = scanKeys(key); // Redis에서 키 목록 검색
		List<TreeMapDomain> treeMapDomains = new ArrayList<>();

		// 배치 작업 생성
		RBatch batch = redissonClient.createBatch();
		List<RBucketAsync<TreeMapDomain>> asyncBuckets = new ArrayList<>();

		// 각 키에 대해 비동기 작업 추가
		keys.forEach(k -> {
			RBucketAsync<TreeMapDomain> bucket = batch.getBucket(k);
			bucket.getAsync();
			asyncBuckets.add(bucket);
		});

		// 배치 실행
		BatchResult<?> result = batch.execute();

		// 결과 처리
		for (int i = 0; i < asyncBuckets.size(); i++) {
			TreeMapDomain domain = (TreeMapDomain) result.getResponses().get(i);
			if (Objects.nonNull(domain)) {
				treeMapDomains.add(domain);
			}
		}

		return treeMapDomains;
	}

	private List<String> scanKeys(String pattern) {
		RKeys keys = redissonClient.getKeys();
		Iterable<String> foundKeys = keys.getKeysByPattern(pattern);
		List<String> keyList = new ArrayList<>();

		foundKeys.forEach(keyList::add);

		return keyList;
	}

}
