package com.bjcareer.search.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class StockTest {

	@Test
	void stock에_stockChart가_없을_때_데이터를_채울_수_있는지_체크() {
		Stock stock = new Stock("003780", "진양산업");
		LocalDate date = stock.calculateStartDayForUpdateStockChart();

		assertEquals(LocalDate.of(1999, 1, 1), date);
	}

	@Test
	void stock에_stockChart가_있을_때_데이터를_채울_수_있는지_체크(){
		Stock stock = new Stock("003780", "진양산업");
		StockChart stockChart = new StockChart();
		OHLC ohlc = new OHLC(1000, 2000, 3000, 4000, LocalDate.of(2021, 1, 1));
		stockChart.addOHLC(List.of(ohlc));

		stock.mergeStockChart(stockChart);

		LocalDate date = stock.calculateStartDayForUpdateStockChart();
		assertEquals(LocalDate.of(2021, 1, 2), date);
	}

	@Test
	void stock에_merge가_진행되는지_테스트(){
		Stock stock = new Stock("003780", "진양산업");

		//과거 차트
		StockChart pastStockChart = new StockChart();
		OHLC ohlc = new OHLC(1000, 2000, 3000, 4000, LocalDate.of(2021, 1, 1));
		pastStockChart.addOHLC(List.of(ohlc));
		stock.mergeStockChart(pastStockChart);

		//현재 갱신 차튼
		StockChart nowStockChart = new StockChart();
		ohlc = new OHLC(1000, 2000, 3000, 4000, LocalDate.of(2021, 1, 2));
		nowStockChart.addOHLC(List.of(ohlc));

		stock.mergeStockChart(nowStockChart);

		List<OHLC> ohlcList = stock.getStockChart().getOhlcList();

		assertEquals(2, ohlcList.size());
		assertEquals(LocalDate.of(2021, 1, 1), ohlcList.get(0).getDate());
		assertEquals(LocalDate.of(2021, 1, 2), ohlcList.get(1).getDate());
	}

}
