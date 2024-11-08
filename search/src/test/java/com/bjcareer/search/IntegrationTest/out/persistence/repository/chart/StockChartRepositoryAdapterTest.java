package com.bjcareer.search.IntegrationTest.out.persistence.repository.chart;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartAboveThresholdCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartSpecificDateCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

@SpringBootTest
class StockChartRepositoryAdapterTest {
	@Autowired
	StockChartRepositoryPort stockChartRepositoryPort;

	@BeforeEach
	@Transactional
	void setUp() {
		Stock stock = new Stock("12345", "진성티이씨");

		OHLC ohlc = new OHLC(100, 200, 3, 4, 100, LocalDate.now());
		OHLC ohlc1 = new OHLC(0, 0, 3, 4, 0, LocalDate.now().plusDays(1));

		List<OHLC> ohlcList = new ArrayList<>();

		ohlcList.add(ohlc);
		ohlcList.add(ohlc1);

		StockChart stockChart = new StockChart(stock.getCode(), ohlcList);
		stockChartRepositoryPort.save(stockChart);
	}

	@Test
	@Transactional
	void testFindOhlcAboveThreshold() {
		String stockCode  = "12345";
		int threshold = 2;

		LoadChartAboveThresholdCommand command = new LoadChartAboveThresholdCommand(stockCode, threshold);
		StockChart chart = stockChartRepositoryPort.findOhlcAboveThreshold(command);

		assertEquals("12345", chart.getStockCode());
		assertEquals(1, chart.getOhlcList().size());
	}

	@Test
	@Transactional
	void 차트에_저장되지_않은_날짜를_불러옴() {
		LoadChartSpecificDateCommand command = new LoadChartSpecificDateCommand("진성티이씨", LocalDate.now().minusDays(1));
		StockChart chart = stockChartRepositoryPort.findChartByDate(command);
		assertEquals(0, chart.getOhlcList().size());
	}
}
