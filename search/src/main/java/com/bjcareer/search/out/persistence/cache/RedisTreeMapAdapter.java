package com.bjcareer.search.out.persistence.cache;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.redisson.api.RMap;
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
		RMap<String, TreeMapDomain> map = redissonClient.getMap(BUKET_KEY);
		domains.forEach(domain -> map.put(domain.getThemaName(), domain));
	}

	public List<TreeMapDomain> getTreemap() {
		RMap<String, TreeMapDomain> map = redissonClient.getMap(BUKET_KEY);
		Collection<TreeMapDomain> treeMapDomains = map.readAllValues();
		return treeMapDomains.stream().collect(Collectors.toList());
	}
}
