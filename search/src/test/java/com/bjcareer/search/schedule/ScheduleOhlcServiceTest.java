package com.bjcareer.search.schedule;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduleOhlcServiceTest {
	@Autowired
	ScheduleOhlcService scheduleOhlcService;
	@Test
	void test() {
		scheduleOhlcService.saveStockInfoAndChartData();
	}
}
