package com.bjcareer.search.out.persistence.stock;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.persistence.stock.LoadStockCommand;
import com.bjcareer.search.domain.entity.Stock;

@SpringBootTest
@Transactional
class StockRepositoryAdapterTest {

	@Autowired
	StockRepositoryAdapter stockRepositoryAdapter;

	@Test
	void 사용자가_입력한_정보만을_불러오는지_테스트() {
		LoadStockCommand command = new LoadStockCommand("삼성");
		List<Stock> stocks = stockRepositoryAdapter.loadAllByKeywordContaining(command);

		for (Stock stock : stocks) {
			boolean contains = stock.getName().contains("삼성");
			assertTrue(contains);
		}
	}
}
