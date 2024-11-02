package com.bjcareer.search.out.api.python;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

@SpringBootTest
class PythonSearchServerAdapterTest {

	@Autowired
	PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void ohlc_요청_테스트() {
		Stock stock = new Stock("003780", "NAVER");
		StockChartQueryConfig config = new StockChartQueryConfig(stock, true);
		StockChart stockChart = pythonSearchServerAdapter.loadStockChart(config);

		assertNotNull(stockChart);
		assertEquals(stock.getCode(), stockChart.getStock().getCode());
		assertNotNull(stockChart.getOhlcList());
	}

	@Test
	void makrket_정보_요청_테스트(){
		List<Stock> stocks = pythonSearchServerAdapter.loadStockInfo(Market.KOSDAQ);
		assertNotNull(stocks);
	}
}
