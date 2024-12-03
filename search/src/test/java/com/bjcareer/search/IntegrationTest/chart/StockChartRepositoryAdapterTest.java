package com.bjcareer.search.IntegrationTest.chart;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartAboveThresholdCommand;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.persistence.chart.StockChartRepositoryAdapter;

@SpringBootTest
class StockChartRepositoryAdapterTest {
	@Autowired
	StockChartRepositoryAdapter stockChartRepositoryAdapter;


	@Test
	void threshold기준으로_데이터_차트를_구성함() {
		// given
		String stockCode = "036890";
		int threshold = 7;
		
		// when
		StockChart ohlcAboveThreshold = stockChartRepositoryAdapter.findOhlcAboveThreshold(
			new LoadChartAboveThresholdCommand(stockCode, threshold));

		// then
		List<OHLC> ohlcList =
			ohlcAboveThreshold.getOhlcList();

	}
}
