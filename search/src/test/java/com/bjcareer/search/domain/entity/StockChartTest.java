package com.bjcareer.search.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bjcareer.search.domain.gpt.GTPNewsDomain;

@ExtendWith(MockitoExtension.class)
class StockChartTest {
	StockChart chart;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV",
		Locale.ENGLISH);
	private final String pubDate = ZonedDateTime.now().format(formatter);

	@BeforeEach
	void setUp() {
		Stock stock = new Stock("370090", "퓨런티어");

		OHLC ohlc = new OHLC(1, 2, 3, 4, 5, 10L,LocalDate.now());
		OHLC ohlc1 = new OHLC(2, 2, 3, 4, 5,10L, LocalDate.now().plusDays(1));
		OHLC ohlc2 = new OHLC(3, 2, 3, 4, 5, 10L,LocalDate.now().plusDays(2));

		List<OHLC> ohlcList = new ArrayList<>();

		ohlcList.add(ohlc);
		ohlcList.add(ohlc1);
		ohlcList.add(ohlc2);

		this.chart = new StockChart(stock.getCode(), ohlcList);
	}

	@Test
	void 주어진_날의_뉴스를_가지고_옴() {
		LocalDate targetDate = LocalDate.now();
		addNewsToOhldUsingDate(targetDate);

		List<GTPNewsDomain> gtpNewsDomains = this.chart.loadNewByDate(targetDate);
		assertEquals(2, gtpNewsDomains.size());
	}

	@Test
	void 주어진_날의_뉴스가_없을때() {
		List<GTPNewsDomain> gtpNewsDomains = this.chart.loadNewByDate(LocalDate.now().plusDays(1));
		assertTrue(gtpNewsDomains.isEmpty());
	}

	@Test
	void 주어진_날짜의_찾아진_뉴스가_있을_때() {
		LocalDate targetDate = LocalDate.now();
		addNewsToOhldUsingDate(targetDate);

		List<GTPNewsDomain> gtpNewsDomains = this.chart.loadNewByDate(targetDate);
		assertEquals(2, gtpNewsDomains.size());
	}

	@Test
	void 주어진_날짜에_뉴스가_없을_때() {
		LocalDate targetDate = LocalDate.now().plusDays(1);
		List<GTPNewsDomain> gtpNewsDomains = this.chart.loadNewByDate(targetDate);
		assertEquals(0, gtpNewsDomains.size());
	}

	@Test
	void 뉴스는_있지만_다음_일정이_없는_뉴스들_일떄() {
		LocalDate targetDate = LocalDate.now();
		addNewsToOhldUsingDate(targetDate);
		List<GTPNewsDomain> gtpNewsDomains = this.chart.getNextSchedule(targetDate);
		assertEquals(0, gtpNewsDomains.size());
	}

	@Test
	void 뉴스는_있지만_다음_일정이_있는_뉴스들() {
		LocalDate nextSchedulerDate = LocalDate.now().plusDays(1);
		List<OHLC> ohlcList = this.chart.getOhlcList();

		Map<String, String> themas = Map.of("자율주행", "트럼프가 좋아함");
		ohlcList.getFirst().addRoseNews(
			new GTPNewsDomain("퓨런티어", "트럼프 당선이 예상되면서 테슬라가 올라서 같이 올랐다", new ArrayList<>(), nextSchedulerDate.toString(), null));

		List<GTPNewsDomain> gtpNewsDomains = this.chart.getNextSchedule(LocalDate.now());
		assertEquals(1, gtpNewsDomains.size());
	}

	@Test
	void 모든_뉴스를_가지고_옴() {
		// given
		//두 개의 날짜에 두 개의 뉴스들을 등록함 총 4개
		addNewsToOhldUsingDate(LocalDate.now());
		addNewsToOhldUsingDate(LocalDate.now().plusDays(1));

		List<GTPNewsDomain> gtpNewsDomains = this.chart.getAllNews();

		assertEquals(4, gtpNewsDomains.size());
	}

	@Test
	void 모든_뉴스를_가지고_옴_하지만_등록된_뉴스는_없음() {
		List<GTPNewsDomain> gtpNewsDomains = this.chart.getAllNews();

		assertEquals(0, gtpNewsDomains.size());
	}

	@Test
	void 주어진_날짜에_뉴스를_링크할_수_있는지() {
		Map<String, String> themas = Map.of("자율주행", "트럼프가 좋아함");
		GTPNewsDomain gtpNewsDomain = new GTPNewsDomain("퓨런티어", "트럼프 당선이 예상되면서 테슬라가 올라서 같이 올랐다", new ArrayList<>(), null, null);
		this.chart.addNewsToOhlc(gtpNewsDomain, LocalDate.now());

		List<GTPNewsDomain> gtpNewsDomains = this.chart.loadNewByDate(LocalDate.now());

		assertEquals(1, gtpNewsDomains.size());
	}

	@Test
	void stock에_stockChart가_없을_때_데이터를_채울_수_있는지_체크() {
		StockChart stockChart = new StockChart();
		LocalDate date = stockChart.calculateStartDayForUpdateStockChart();

		assertEquals(LocalDate.of(1999, 1, 1), date);
	}

	@Test
	void stockChart는_언제_데이터_부터_갱신이_필요한지_계산할_수_있어야_함(){
		StockChart stockChart = new StockChart();

		OHLC ohlc = new OHLC(1000, 2000, 3000, 4000, 100, 10L,LocalDate.of(2021, 1, 1));
		stockChart.addOHLC(List.of(ohlc));

		LocalDate date = stockChart.calculateStartDayForUpdateStockChart();
		assertEquals(LocalDate.of(2021, 1, 2), date);
	}

	@Test
	void stockchart에_ohlc가_merger되는지_확인(){
		//과거 차트
		StockChart pastStockChart = new StockChart();
		OHLC ohlc = new OHLC(1000, 2000, 3000, 4000, 20, 10L,LocalDate.of(2021, 1, 1));
		pastStockChart.addOHLC(List.of(ohlc));

		//현재 갱신 차튼
		StockChart nowStockChart = new StockChart();
		ohlc = new OHLC(1000, 2000, 3000, 4000, 20,10L, LocalDate.of(2021, 1, 2));
		nowStockChart.addOHLC(List.of(ohlc));

		nowStockChart.mergeOhlc(nowStockChart);
		List<OHLC> ohlcList = nowStockChart.getOhlcList();

		assertEquals(2, ohlcList.size());
	}

	private void addNewsToOhldUsingDate(LocalDate date) {
		List<OHLC> ohlcList = this.chart.getOhlcList();
		Map<String, String> themas = Map.of("자율주행", "트럼프가 좋아함");

		for (OHLC ohlc : ohlcList) {
			if (ohlc.getDate().isEqual(date)) {
				ohlc.addRoseNews(new GTPNewsDomain("퓨런티어", "트럼프 당선이 예상되면서 테슬라가 올라서 같이 올랐다", new ArrayList<>(), null, null));
				ohlc.addRoseNews(new GTPNewsDomain("퓨런티어", "트럼프 당선 확률이 높아지면서 올랐다", new ArrayList<>(), null, null));
			}
		}
	}
}
