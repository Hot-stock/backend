package com.bjcareer.search.application.information;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.exceptions.InvalidStockInformation;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartSpecificDateCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.domain.entity.StockChart;

@SpringBootTest
class NewsServiceTest {
	@Autowired
	NewsService newsService;

	@Autowired
	StockChartRepositoryPort stockChartRepositoryPort;

	@Test
	void 데이터베이스에_ohlc_데이터가_없을때() {
		//given
		String stockName = "우신시스템";
		LocalDate date = LocalDate.now().plusDays(1);

		//when
		Optional<GTPNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stockName, date);
		assertTrue(raiseReasonThatDate.isEmpty());
	}

	@Test
	@Transactional
	void 검색될_뉴스가_있고_저장이_가능한지_테스트() {
		String stockName = "우신시스템";
		LocalDate date = LocalDate.of(2024, 10, 25);

		Optional<GTPNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stockName, date);
		assertTrue(raiseReasonThatDate.isPresent());

		LoadChartSpecificDateCommand command = new LoadChartSpecificDateCommand(stockName, date);
		StockChart chartByDate = stockChartRepositoryPort.findChartByDate(command);

		assertTrue(chartByDate.loadNewByDate(date).isPresent());
	}

	@Test
	@Transactional
	void 없는_주식을_요청함() {
		String stockName = "우신시스템2";
		LocalDate date = LocalDate.of(2024, 10, 25);

		assertThrows(InvalidStockInformation.class, () -> newsService.findRaiseReasonThatDate(stockName, date));
	}

	@Test
	@Transactional(readOnly = true)
	void 다음_일정을_요청함() {
		String stockName = "우신시스템";
		LocalDate date = LocalDate.of(1999,11,1);

		List<GTPNewsDomain> nextSchedule = newsService.findNextSchedule(stockName, date);
		System.out.println("nextSchedule = " + nextSchedule);
	}
}
