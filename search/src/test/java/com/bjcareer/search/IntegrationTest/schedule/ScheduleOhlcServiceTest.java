package com.bjcareer.search.IntegrationTest.schedule;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.persistence.repository.stock.StockRepository;
import com.bjcareer.search.schedule.ScheduleOhlcService;

//통합테스트
@SpringBootTest
class ScheduleOhlcServiceTest {
	@Autowired
	ScheduleOhlcService scheduleOhlcService;

	@Autowired
	StockRepository stockRepository;

	@Test
	void 모든_주식의_정보를_불러와서_저장함() {
		scheduleOhlcService.saveStockInfoAndChartData();

		Optional<Stock> optionalStock = stockRepository.findByName("삼성전자");
		assertTrue(optionalStock.isEmpty());

		Stock stock = optionalStock.get();
		StockChart stockChart = stock.getStockChart();

		assertNotNull(stockChart);
		assertTrue(stockChart.getOhlcList().isEmpty());
	}

}
