package com.bjcareer.search.IntegrationTest.application.information;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.information.NewsService;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartSpecificDateCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.persistence.repository.stock.StockRepository;

@SpringBootTest
@Transactional
class NewsServiceTest {
	@Autowired
	NewsService newsService;

	@Autowired
	StockRepository stockRepositoryPort;

	@Autowired
	StockChartRepositoryPort stockChartRepositoryPort;

	@Test
	void 데이터베이스에_ohlc_데이터가_없을때() {
		//given
		String stockName = "우신시스템";
		LocalDate date = LocalDate.now().plusDays(1);

		//when
		List<GTPNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stockName, date);
		assertTrue(raiseReasonThatDate.isEmpty());
	}

	@Test
	void 검색될_뉴스가_있고_저장이_가능한지_테스트() {
		String stockName = "우신시스템";
		LocalDate date = LocalDate.of(2024, 10, 25);

		List<GTPNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stockName, date);
		assertFalse(raiseReasonThatDate.isEmpty());

		LoadChartSpecificDateCommand command = new LoadChartSpecificDateCommand(stockName, date);
		StockChart chartByDate = stockChartRepositoryPort.findChartByDate(command);

		assertFalse(chartByDate.loadNewByDate(date).isEmpty());
	}

	@Test
	@Transactional
	void 없는_주식을_요청함() {
		String stockName = "우신시스템2";
		LocalDate date = LocalDate.of(2024, 10, 25);

		assertThrows(IllegalArgumentException.class, () -> newsService.findRaiseReasonThatDate(stockName, date));
	}

	@Test
	void ohlc기_없는_상태에서_다음_일정을_요청함() {
		String stockName = "우신시스템";
		LocalDate date = LocalDate.of(1999,11,1);

		Stock stock = new Stock("017370", stockName);
		stockRepositoryPort.save(stock);

		StockChart stockChart = new StockChart(stock, new ArrayList<>());
		stockChartRepositoryPort.save(stockChart);

		assertDoesNotThrow(() -> newsService.findNextSchedule(stockName, date));
	}

	@Test
	void 상승_이유를_찾아서_저장이_되는지_체크() {
		String stockName = "우신시스템";
		Stock stock = new Stock("017370", stockName);

		stockRepositoryPort.save(stock);

		StockChart stockChart = new StockChart(stock,
			List.of(new OHLC(1000, 2000, 3000, 4000, 20, LocalDate.of(2020, 9, 2))));
		stockChartRepositoryPort.save(stockChart);

		List<GTPNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate("우신시스템",
			LocalDate.of(2020, 9, 2));
		assertFalse(raiseReasonThatDate.isEmpty());

		LoadChartSpecificDateCommand command = new LoadChartSpecificDateCommand("우신시스템", LocalDate.of(2020, 9, 2));
		StockChart chartByDate = stockChartRepositoryPort.findChartByDate(command);
		List<GTPNewsDomain> allNews = chartByDate.getAllNews();

		for (GTPNewsDomain gtpNewsDomain : allNews) {
			System.out.println(gtpNewsDomain);
		}
	}
}