package com.bjcareer.search.IntegrationTest.application.search;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.search.RankingService;
import com.bjcareer.search.out.persistence.cache.CacheRepository;

@SpringBootTest
class RankingServiceTest {
	@Autowired
	RankingService rankingService;
	@Autowired
	CacheRepository cacheRepository;

	@Test
	void 주어진_키워드로_랭킹을_업데이트하는지() {
		//given
		String keyword = "테스트";

		//when
		rankingService.updateKeyword(keyword);
		Double rankingScore = cacheRepository.getRankingScore(keyword);
		cacheRepository.deleteRanking(keyword);

		//then
		assertTrue(rankingScore < 0);
	}
}
