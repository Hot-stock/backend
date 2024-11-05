package com.bjcareer.search.application.information;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewsServiceTest {
	@Autowired
	NewsService newsService;

	@Test
	void 잘못된_날짜를_요청했을_때() {
		//given
		String stockName = "우신시스템";
		LocalDate date = LocalDate.of(2024, 11, 5);

		newsService.findRaiseReasonThatDate("우신시스템", date);
	}
}
