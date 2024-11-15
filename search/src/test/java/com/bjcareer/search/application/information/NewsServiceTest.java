package com.bjcareer.search.application.information;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bjcareer.search.CommonConfig;
import com.bjcareer.search.application.exceptions.InvalidStockInformationException;
import com.bjcareer.search.application.port.out.api.GPTNewsPort;
import com.bjcareer.search.application.port.out.api.LoadNewsPort;
import com.bjcareer.search.application.port.out.api.LoadStockInformationPort;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

	@Mock
	LoadNewsPort loadNewsPort;
	@Mock
	LoadStockInformationPort loadStockInformationServerPort;

	@Mock
	StockRepositoryPort stockRepositoryPort;
	@Mock
	StockChartRepositoryPort stockChartRepositoryPort;

	@Mock
	GPTNewsPort gptAPIPort;

	@InjectMocks
	NewsService newsService;

	Set<String> thema = Set.of("수영복");

	@Test
	void 주식이_없는_상태에서_요청을_진행하면_오류를_반환해야함() {
		// Given
		String stockName = "없는 주식";
		LocalDate date = LocalDate.now();

		assertThrows(InvalidStockInformationException.class,
			() -> newsService.findRaiseReasonThatDate(stockName, date));
	}

	@Test
	void 주식의_상승이유를_찾을_수_없을_때_빈_객체를_반환() {
		// Given
		String stockName = "배럴";
		LocalDate date = LocalDate.now();

		Stock stock = new Stock("12345", stockName);
		StockChart stockChart = new StockChart(stock.getCode(), new ArrayList<>());

		OHLC ohlc = new OHLC(100, 200, 3, 4, 100, 10L, LocalDate.now().minusDays(1));
		OHLC ohlc1 = new OHLC(0, 0, 3, 4, 2, 10L, LocalDate.now().plusDays(1));

		stockChart.addOHLC(List.of(ohlc, ohlc1));

		when(stockRepositoryPort.findByName(anyString())).thenReturn(Optional.of(stock));
		when(stockChartRepositoryPort.loadStockChart(stock.getCode())).thenReturn(Optional.of(stockChart));

		StockChart tempChart = new StockChart(stock.getCode(), new ArrayList<>());
		tempChart.addOHLC(List.of(new OHLC(100, 200, 3, 4, 100, 10L, date)));
		when(loadStockInformationServerPort.loadStockChart(any())).thenReturn(tempChart);

		// When
		List<GTPNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stockName, date);

		// Then
		assertTrue(raiseReasonThatDate.isEmpty());
	}

	@Test
	void 주식의_상승이유를_찾을_수_있을_떄_GPTNEWDomain객체를_반환() {
		// Given
		String pubDate = CommonConfig.createPubDate();

		String stockName = "배럴";
		LocalDate date = LocalDate.now();
		News news = new News("title", "link", "link", "묘사", pubDate, "휴가로 인해서 래쉬가드 수요가 증가함");

		Map<String, String> themas = Map.of("수영복", "휴가로 인해서 래쉬가드 수요가 증가함");
		GTPNewsDomain gtpNewsDomain = new GTPNewsDomain("배럴", "휴가로 인해서 래쉬가드 수요가 증가함", new ArrayList<>(), null, null);
		gtpNewsDomain.addNewsDomain(news);

		OHLC ohlc = new OHLC(100, 200, 3, 4, 100, 10L, LocalDate.now());
		OHLC ohlc1 = new OHLC(0, 0, 3, 4, 2, 10L, LocalDate.now().plusDays(1));

		List<OHLC> ohlcList = new ArrayList<>();
		ohlcList.add(ohlc);
		ohlcList.add(ohlc1);

		Stock stock = new Stock("12345", stockName);
		StockChart stockChart = new StockChart(stock.getCode(), ohlcList);

		stockChart.addOHLC(List.of(ohlc, ohlc1));

		when(stockRepositoryPort.findByName(anyString())).thenReturn(Optional.of(stock));
		when(stockChartRepositoryPort.loadStockChart(stock.getCode())).thenReturn(Optional.of(stockChart));
		when(loadNewsPort.fetchNews(any())).thenReturn(List.of(news));
		when(gptAPIPort.findStockRaiseReason(anyString(), anyString(), anyString(), any())).thenReturn(
			Optional.of(gtpNewsDomain));

		List<GTPNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stockName, date);

		// Then
		assertFalse(raiseReasonThatDate.isEmpty());
		assertTrue(raiseReasonThatDate.getFirst().getNews().equals(news));
	}

	@Test
	void 주식의_상승_이유가_여러_개여서_getNew가_여러개를_반환해야함() {
		String pubDate = CommonConfig.createPubDate();

		String stockName = "배럴";

		News news1 = new News("title", "link", "link", "묘사", pubDate, "휴가로 인해서 래쉬가드 수요가 증가함");
		News news2 = new News("title", "link", "link", "묘사", pubDate, "동남아 여행지로 많이 가서 수요가 증가함");

		Map<String, String> themas = Map.of("수영복", "휴가로 인해서 래쉬가드 수요가 증가함");
		GTPNewsDomain gtpNewsDomain = new GTPNewsDomain("배럴", "휴가로 인해서 래쉬가드 수요가 증가함", new ArrayList<>(), null, null);
		gtpNewsDomain.addNewsDomain(news1);

		Map<String, String> themas2 = Map.of("여행", "동남아 여행객 증가로 래쉬가드 수요 증가");
		GTPNewsDomain gtpNewsDomain2 = new GTPNewsDomain("배럴", "동남아 여행지로 많이 가서 수요가 증가함", new ArrayList<>(), null, null);
		gtpNewsDomain2.addNewsDomain(news2);

		Stock stock = new Stock("12345", stockName);

		OHLC ohlc = new OHLC(100, 200, 3, 4, 100, 10L, LocalDate.now());
		OHLC ohlc1 = new OHLC(0, 0, 3, 4, 2, 10L, LocalDate.now().plusDays(1));

		ohlc.addRoseNews(gtpNewsDomain);
		ohlc.addRoseNews(gtpNewsDomain2);

		StockChart stockChart = new StockChart(stock.getCode(), new ArrayList<>(List.of(ohlc, ohlc1)));

		when(stockRepositoryPort.findByName(anyString())).thenReturn(Optional.of(stock));
		when(stockChartRepositoryPort.loadStockChart(stock.getCode())).thenReturn(Optional.of(stockChart));

		List<GTPNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stockName, LocalDate.now());

		assertEquals(2, raiseReasonThatDate.size());
	}
}
