package com.bjcareer.GPTService.schedule;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SearchRankingServiceTest {
	@Autowired SearchRankingService searchRankingService;

	@Test
	void  testUpdateRanking() {
		searchRankingService.updateRanking();
	}
}
