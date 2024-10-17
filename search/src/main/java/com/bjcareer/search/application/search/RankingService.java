package com.bjcareer.search.application.search;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bjcareer.search.application.port.in.RankingUsecase;
import com.bjcareer.search.out.persistence.repository.cache.CacheRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingService implements RankingUsecase {
	private final CacheRepository cacheRepository;

	public void updateKeyword(String keyword) {
		log.debug("Keyword to update is {}", keyword);
		Double v = cacheRepository.updateRanking(keyword);
		log.debug("keyword count is {}", v);
	}

	public List<String> getRankKeyword(Integer index) {
		return cacheRepository.getRanking(index);
	}
}
