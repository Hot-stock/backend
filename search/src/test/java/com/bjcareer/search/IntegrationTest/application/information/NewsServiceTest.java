package com.bjcareer.search.IntegrationTest.application.information;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.exceptions.InvalidStockInformationException;
import com.bjcareer.search.application.information.NewsService;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartAboveThresholdCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartSpecificDateCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

@SpringBootTest
@Transactional
class NewsServiceTest {
	@Autowired
	NewsService newsService;

	@Autowired
	StockRepositoryPort stockRepositoryPort;

	@Autowired
	StockChartRepositoryPort stockChartRepositoryPort;

	String stockCode = "017370";
	String stockName = "우신시스템";

	// @BeforeEach
	// @Transactional
	void setUp() {
		Stock stock = new Stock(stockCode, stockName);
		stockRepositoryPort.save(stock);

		StockChart stockChart = new StockChart(stock.getCode(),
			List.of(new OHLC(1000, 2000, 3000, 4000, 20, 10L,LocalDate.now())));

		stockChartRepositoryPort.save(stockChart);
	}

	@Test
	void 데이터베이스에_ohlc_데이터가_없을때() {
		//given
		String stockName = "우신시스템";
		LocalDate date = LocalDate.now().plusDays(1);

		//when
		List<GPTNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stockName, date);
		assertTrue(raiseReasonThatDate.isEmpty());
	}

	@Test
	void 검색될_뉴스가_있고_저장이_가능한지_테스트() {
		String stockName = "우신시스템";
		LocalDate date = LocalDate.of(2024, 10, 25);

		List<GPTNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stockName, date);
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

		assertThrows(InvalidStockInformationException.class,
			() -> newsService.findRaiseReasonThatDate(stockName, date));
	}

	@Test
	void ohlc기_없는_상태에서_다음_일정을_요청함() {
		String stockName = "우신시스템1";
		LocalDate date = LocalDate.of(1999,11,1);

		Stock stock = new Stock("017371", stockName);
		stockRepositoryPort.save(stock);

		StockChart stockChart = new StockChart(stock.getCode(), new ArrayList<>());
		stockChartRepositoryPort.save(stockChart);

		assertDoesNotThrow(() -> newsService.findNextSchedule(stockName, date));
	}

	@Test
	void 상승_이유를_찾아서_저장이_되는지_체크() {
		//given
		List<OHLC> ohlcList = List.of(new OHLC(1000, 2000, 3000, 4000, 20,10L, LocalDate.of(2020, 9, 2)));
		StockChart chart = stockChartRepositoryPort.loadStockChart(stockCode).get();
		chart.addOHLC(ohlcList);

		//when
		List<GPTNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate("우신시스템",
			LocalDate.of(2020, 9, 2));

		assertFalse(raiseReasonThatDate.isEmpty());

		LoadChartSpecificDateCommand command = new LoadChartSpecificDateCommand("우신시스템", LocalDate.of(2020, 9, 2));
		StockChart chartByDate = stockChartRepositoryPort.findChartByDate(command);
		List<GPTNewsDomain> allNews = chartByDate.getAllNews();

		for (GPTNewsDomain GPTNewsDomain : allNews) {
			System.out.println(GPTNewsDomain);
		}
	}

	@Test
	@Rollback(false)
	void 특정_종목의_모든_뉴스에_뉴스데이터를_저장() {
		//given
		String stockName = "진성티이씨";
		LocalDate date = LocalDate.of(2020, 9, 2);

		StockChart chart = stockChartRepositoryPort.findOhlcAboveThreshold(
			new LoadChartAboveThresholdCommand("036890", 7));

		for (OHLC ohlc : chart.getOhlcList()) {
			System.out.println("ohlc.getDate() = " + ohlc.getDate());

			if (ohlc.getNews().isEmpty()) {
				newsService.findRaiseReasonThatDate(stockName, ohlc.getDate());
			}
		}
	}
}
