package com.bjcareer.search.IntegrationTest.schedule;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.bjcareer.search.application.port.out.persistence.ranking.MarketRankingPort;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;
import com.bjcareer.search.schedule.ScheduleRankService;

@SpringBootTest
class ScheduleRankServiceTest {
	@Autowired
	ScheduleRankService scheduleRankService;

	@Autowired
	MarketRankingPort marketRankingPort;

	@Test
	@Rollback(value = false)
	void 스케쥴링을_통해서_레디스에_저장되는지_테스트() {
		scheduleRankService.run();
		List<GTPNewsDomain> rankingNews = marketRankingPort.getRankingNews();
		assertFalse(rankingNews.isEmpty());
	}
}
