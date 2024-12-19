package com.bjcareer.GPTService.out.persistence.rdb;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.Stock;

@SpringBootTest
class StockRepositoryTest {
	@Autowired
	StockRepository stockRepository;

	@Test
	void testFindByName() {

		Stock foundStock = stockRepository.findByName("배럴").orElseThrow();
		assertEquals("배럴", foundStock.getName());
	}

	@Test
	void findAll() {

		List<Stock> stocks = stockRepository.findAll();
		Map<String, String> stockMap = new HashMap<>();

		for (Stock stock : stocks) {
			stockMap.put(stock.getName(), stock.getCode());
			System.out.println("stock = " + stock);
		}
	}
}
