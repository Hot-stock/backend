package com.bjcareer.search.IntegrationTest.out.persistence.repository.cache;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.port.out.persistence.ranking.MarketRankingPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;

@SpringBootTest
class RedisMarketRankAdapterTest {
	@Autowired
	MarketRankingPort marketRankingPort;

	Stock stock = new Stock("370090", "퓨런티어");
	GTPNewsDomain gtpNewsDomain = new GTPNewsDomain("퓨런티어", "test", null, "2021-08-01", "test");

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
		marketRankingPort.removeRankingNews(stock);
	}

	@Test
	void stock이랑news가update되는지_return_true() {
		marketRankingPort.updateRankingNews(gtpNewsDomain, stock);
		boolean existInCache = marketRankingPort.isExistInCache(stock);
		assertTrue(existInCache);
	}

	@Test
	void 저장된_뉴스가_불러와지는지_테스트함() {
		marketRankingPort.updateRankingNews(gtpNewsDomain, stock);
		List<GTPNewsDomain> rankingNews = marketRankingPort.getRankingNews();
		System.out.println("rankingNews = " + rankingNews);
		assertFalse(rankingNews.isEmpty());
	}
}
