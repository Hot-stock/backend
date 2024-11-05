package com.bjcareer.search.application.information;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bjcareer.search.application.port.out.api.GPTAPIPort;
import com.bjcareer.search.application.port.out.api.LoadNewsPort;
import com.bjcareer.search.application.port.out.api.LoadStockInformationPort;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

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
	GPTAPIPort gptAPIPort;

	@InjectMocks
	NewsService newsService;

	@Test
	void 주식이_없는_상태에서_요청을_진행하면_오류를_반환해야함() {
		// Given
		String stockName = "없는 주식";
		LocalDate date = LocalDate.now();

		assertThrows(IllegalArgumentException.class, () -> newsService.findRaiseReasonThatDate(stockName, date));
	}

	@Test
	void 주식의_상승이유를_찾을_수_없을_때_빈_객체를_반환() {
		// Given
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV", Locale.ENGLISH);
		String pubDate = ZonedDateTime.now().format(formatter);

		String stockName = "배럴";
		LocalDate date = LocalDate.now();

		Stock stock = new Stock("12345", stockName);
		StockChart stockChart = new StockChart();

		OHLC ohlc = new OHLC(100, 200, 3, 4, 100, LocalDate.now().minusDays(1));
		OHLC ohlc1 = new OHLC(0, 0, 3, 4, 2, LocalDate.now().plusDays(1));

		stockChart.addOHLC(List.of(ohlc, ohlc1));
		stock.mergeStockChart(stockChart);

		when(stockRepositoryPort.findByName(anyString())).thenReturn(Optional.of(stock));
		when(stockChartRepositoryPort.loadStockChart(stock.getCode())).thenReturn(stockChart);

		StockChart tempChart = new StockChart();
		tempChart.addOHLC(List.of(new OHLC(100, 200, 3, 4, 100, date)));
		when(loadStockInformationServerPort.loadStockChart(any())).thenReturn(tempChart);

		// When
		Optional<GTPNewsDomain> result = newsService.findRaiseReasonThatDate(stockName, date);

		// Then
		assertTrue(result.isEmpty());
	}

	@Test
	void 주식의_상승이유를_찾을_수_있을_떄_GPTNEWDomain객체를_반환() {
		// Given
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV", Locale.ENGLISH);
		String pubDate = ZonedDateTime.now().format(formatter);

		String stockName = "배럴";
		LocalDate date = LocalDate.now();
		News news = new News("title", "link", "link", "묘사", pubDate, "휴가로 인해서 래쉬가드 수요가 증가함");

		GTPNewsDomain gtpNewsDomain = new GTPNewsDomain("배럴", "휴가로 인해서 래쉬가드 수요가 증가함", "수영복", null, null);
		gtpNewsDomain.addNewsDomain(news);


		Stock stock = new Stock("12345", stockName);
		StockChart stockChart = new StockChart();

		OHLC ohlc = new OHLC(100, 200, 3, 4, 100, LocalDate.now());
		OHLC ohlc1 = new OHLC(0, 0, 3, 4, 2, LocalDate.now().plusDays(1));

		stockChart.addOHLC(List.of(ohlc, ohlc1));
		stock.mergeStockChart(stockChart);

		when(stockRepositoryPort.findByName(anyString())).thenReturn(Optional.of(stock));
		when(stockChartRepositoryPort.loadStockChart(stock.getCode())).thenReturn(stockChart);
		when(loadNewsPort.fetchNews(any())).thenReturn(List.of(news));
		when(gptAPIPort.findStockRaiseReason(anyString(), anyString(), any())).thenReturn(Optional.of(gtpNewsDomain));

		Optional<GTPNewsDomain> result = newsService.findRaiseReasonThatDate(stockName, date);

		// Then
		assertTrue(result.isPresent());
		assertTrue(result.get().getNews().equals(news));
	}
}
