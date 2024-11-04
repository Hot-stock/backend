package com.bjcareer.search.domain.entity;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.bjcareer.search.out.persistence.repository.stock.StockRepository;

@SpringBootTest
public class StockSpringBootTest {
	@Autowired
	StockRepository stockRepository;

	@Test
	@Rollback(false)
	void test_StockRepository() {
		Stock stock = new Stock("003780", "진양산업");

		//과거 차트
		StockChart stockChart = new StockChart();
		OHLC ohlc = new OHLC(1000, 2000, 3000, 4000, 20, LocalDate.of(2021, 1, 1));
		stockChart.addOHLC(List.of(ohlc));

		stock.mergeStockChart(stockChart);


		stockRepository.save(stock);
	}
}
