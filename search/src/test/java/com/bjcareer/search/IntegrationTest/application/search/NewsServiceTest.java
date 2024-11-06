package com.bjcareer.search.IntegrationTest.application.search;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.bjcareer.search.application.information.NewsService;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartSpecificDateCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.domain.entity.StockChart;

@SpringBootTest
class NewsServiceTest {
	@Autowired
	NewsService newsService;
	@Autowired
	StockChartRepositoryPort stockChartRepositoryPort;

	@Test
	@Rollback(false)
	void 상승_이유를_찾아서_저장이_되는지_체크() {
		Optional<GTPNewsDomain> result = newsService.findRaiseReasonThatDate("우신시스템", LocalDate.of(2020, 9, 2));
		assertTrue(result.isPresent());

		LoadChartSpecificDateCommand command = new LoadChartSpecificDateCommand("우신시스템", LocalDate.of(2020, 9, 2));
		StockChart chartByDate = stockChartRepositoryPort.findChartByDate(command);
	}
}
