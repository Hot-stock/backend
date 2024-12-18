package com.bjcareer.search.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.ThemaInfo;

class TreeMapDomainTest {

	@Test
	void test_테마의_히트맵이_구성되는지() {
		// given
		ThemaInfo themaInfo = new ThemaInfo("수영복");
		Stock stock = new Stock("1234", "배럴");
		Map<Stock, StockChart> chartMap = new HashMap<>();

		OHLC ohlc1 = new OHLC(275, 263, 266, 266, -1, 9057L, LocalDate.now());
		OHLC ohlc2 = new OHLC(275, 263, 266, 266, -1, 9057L, LocalDate.now());
		OHLC ohlc3 = new OHLC(275, 263, 266, 266, -1, 9057L, LocalDate.now());
		List<OHLC> ohlcs = List.of(ohlc1, ohlc2, ohlc3);
		StockChart chart = new StockChart("1234", ohlcs);
		chartMap.put(stock, chart);

		TreeMapDomain treeMapDomain = new TreeMapDomain(themaInfo, chartMap, 3);

		Double increaseRate = treeMapDomain.getIncreaseRate();
		Double i = treeMapDomain.getStockIncreaseRate().get(stock);

		assertEquals(-1, increaseRate, "테마의 히트맵이 구성되어야 한다.");
		assertEquals(-1, i, "주식의 상승률이 구성되어야 한다.");
	}
}
