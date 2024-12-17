package com.bjcareer.GPTService.schedule;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduleUpdateRaiseReasonTest {
	@Autowired
	ScheduleUpdateRaiseReason scheduleUpdateRaiseReason;

	@Test
	void test_업데이트_이유_검증() {
		scheduleUpdateRaiseReason.updateRankingKeyword();
	}
}
