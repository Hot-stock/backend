package com.bjcareer.search.IntegrationTest.out.api.python;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.api.NewsCommand;
import com.bjcareer.search.application.port.out.api.StockChartQueryCommand;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.api.python.PythonSearchServerAdapter;

@SpringBootTest
class PythonSearchServerAdapterTest {
	@Autowired
	PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void ohlc_요청_테스트() {
		Stock stock = new Stock("017370", "우신시스템");
		StockChartQueryCommand command = new StockChartQueryCommand(stock, true);
		StockChart stockChart = pythonSearchServerAdapter.loadStockChart(command);

		assertNotNull(stockChart);
		assertEquals(stock.getCode(), stockChart.getStock().getCode());

		for (int i = 0; i < stockChart.getOhlcList().size(); i++) {
			if (stockChart.getOhlcList().get(i).getDate().equals(LocalDate.of(2020, 9, 2))) {
				System.out.println("stockChart = " + stockChart.getOhlcList().get(i).getPercentageIncrease());
			}
		}
	}

	@Test
	@Transactional
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

	@Test
	void 특정일의_뉴스를_가지고_오는지_테스트() {
		// given
		LocalDate baseDate = LocalDate.of(2018, 11, 8);
		NewsCommand command = new NewsCommand("진성티이씨", baseDate, baseDate);
		List<News> news = pythonSearchServerAdapter.fetchNews(command);
		assertNotNull(news);
	}

	@Test
	void 상승률top10요청테스트() {
		List<Stock> stocks = pythonSearchServerAdapter.loadRanking(Market.KOSDAQ);
		assertEquals(10, stocks.size());
	}
}
