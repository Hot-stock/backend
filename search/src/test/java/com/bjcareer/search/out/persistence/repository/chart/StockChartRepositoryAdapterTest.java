package com.bjcareer.search.out.persistence.repository.chart;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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
import com.bjcareer.search.domain.entity.StockChart;

@SpringBootTest
class StockChartRepositoryAdapterTest {
	@Autowired
	StockChartRepositoryPort stockChartRepositoryPort;

	@BeforeEach
	@Transactional
	void setUp() {
		OHLC ohlc = new OHLC(100, 200, 3, 4, 100, LocalDate.now());
		OHLC ohlc1 = new OHLC(0, 0, 3, 4, 2, LocalDate.now());

		StockChart stockChart = new StockChart(null, List.of(ohlc, ohlc1));
		stockChartRepositoryPort.save(stockChart);
	}

	@Test
	@Transactional
	void testFindOhlcAboveThreshold() {
		String stockCode  = "12345";
		int threshold = 2;
		LoadChartAboveThresholdCommand command = new LoadChartAboveThresholdCommand(stockCode, threshold);
		StockChart chart = stockChartRepositoryPort.findOhlcAboveThreshold(command);

		assertEquals("12345", chart.getStock().getCode());
		assertEquals(1, chart.getOhlcList().size());
	}

	@Test
	void test_없는_날짜의_차트를_불려오려고_힘() {
		LoadChartSpecificDateCommand command = new LoadChartSpecificDateCommand("12345", LocalDate.now());
		StockChart chart = stockChartRepositoryPort.findChartByDate(command);
		assertEquals(0, chart.getOhlcList().size());
	}
}
