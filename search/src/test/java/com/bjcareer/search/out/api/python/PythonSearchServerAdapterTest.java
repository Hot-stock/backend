package com.bjcareer.search.out.api.python;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.port.out.NewsCommand;
import com.bjcareer.search.domain.News;
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

	@Test
	void news_요청_테스트() {
		NewsCommand command = new NewsCommand("코로나", LocalDate.of(2021, 3, 3), LocalDate.of(2021, 3, 3));
		List<News> news = pythonSearchServerAdapter.fetchNews(command);
		assertNotNull(news);
	}
}
