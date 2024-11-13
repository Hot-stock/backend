package com.bjcareer.search.IntegrationTest.schedule;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.schedule.ScheduleOhlcService;

//통합테스트
@SpringBootTest
class ScheduleOhlcServiceTest {
	@Autowired
	ScheduleOhlcService scheduleOhlcService;

	@Autowired
	StockChartRepositoryPort stockChartRepositoryPort;

	@Autowired
	StockRepositoryPort stockRepository;

	@Test
	void 모든_주식의_정보를_불러와서_저장함() {
		scheduleOhlcService.saveStockInfoAndChartData();

		Optional<Stock> optionalStock = stockRepository.findByName("삼성전자");
		assertTrue(optionalStock.isEmpty());

		Stock stock = optionalStock.get();
		Optional<StockChart> stockChart = stockChartRepositoryPort.loadStockChart(stock.getCode());

		assertNotNull(stock);
		assertTrue(stockChart.isPresent());
	}

}
