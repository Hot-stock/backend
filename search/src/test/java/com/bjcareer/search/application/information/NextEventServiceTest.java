package com.bjcareer.search.application.information;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

@SpringBootTest
class NextEventServiceTest {
	@Autowired
	NextEventService nextEventService;


	@Test
	void 특정_주식의_다음이벤트_식별() {
		List<GPTStockNewsDomain> gptStockNewsDomains = nextEventService.filterUpcomingEventsByStockName("900310");
		System.out.println("gptStockNewsDomains = " + gptStockNewsDomains);
	}
}
