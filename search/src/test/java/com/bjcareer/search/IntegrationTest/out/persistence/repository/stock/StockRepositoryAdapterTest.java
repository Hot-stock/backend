package com.bjcareer.search.IntegrationTest.out.persistence.repository.stock;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.persistence.repository.stock.StockRepositoryAdapter;

@SpringBootTest
class StockRepositoryAdapterTest {
	@Autowired
	StockRepositoryAdapter stockRepositoryAdapter;

	@BeforeEach
	@Transactional
	void setUp() {
		Stock stock = new Stock("12345", "BYE");
		StockChart stockChart = new StockChart(stock.getCode(), new ArrayList<>());
		OHLC ohlc = new OHLC(100, 200, 3, 4,100,  LocalDate.now());
		OHLC ohlc1 = new OHLC(0, 0, 3, 4,2,  LocalDate.now());

		stockChart.addOHLC(List.of(ohlc, ohlc1));

		stockRepositoryAdapter.save(stock);
	}

	@Test
	@Transactional
	void testFindByCode() {
		Optional<Stock> byCode = stockRepositoryAdapter.findByCode("12345");
		assertTrue(byCode.isPresent());
		assertEquals("12345", byCode.get().getCode());
	}

}
