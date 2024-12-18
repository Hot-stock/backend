package com.bjcareer.search.schedule;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.search.application.TreeMapService;
import com.bjcareer.search.domain.TreeMapDomain;
import com.bjcareer.search.out.persistence.cache.RedisTreeMapAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleTreeMapService {
	private final TreeMapService treeMapService;
	private final RedisTreeMapAdapter redisTreeMapAdapter;

	@Scheduled(fixedDelay = 30000)
	public void makeTrie() {
		log.debug("TrieService started");
		List<TreeMapDomain> domains = treeMapService.calcTreeMap(3);
		redisTreeMapAdapter.uploadTreemap(domains);
		log.debug("TrieService finished");
	}
}
