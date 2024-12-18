package com.bjcareer.search.out.persistence.chart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.StockChart;

@SpringBootTest
class StockChartRepositoryAdapterTest {

	@Autowired
	StockChartRepositoryAdapter stockChartRepositoryAdapter;

	@Test
	@Transactional
	@Rollback
	void stockChart안에_있는_ohlc_저장테스트() {
		// given
		OHLC ohlc = new OHLC(100, 200, 300, 400, 500, 600L, LocalDate.now());
		StockChart stockChart = new StockChart("1234", new ArrayList<>(List.of(ohlc)));
		// when
		stockChartRepositoryAdapter.save(stockChart);
		// then
	}
}
