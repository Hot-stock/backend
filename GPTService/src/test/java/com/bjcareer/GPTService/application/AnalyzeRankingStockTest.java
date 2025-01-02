package com.bjcareer.GPTService.application;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.Stock;

@SpringBootTest
class AnalyzeRankingStockTest {

	@Autowired
	AnalyzeRankingStock analyzeRankingStock;

	@Autowired
	GPTStockAnalyzeService gptStockAnalyzeService;

	@Test
	void 랭킹인스아트() {
		List<Stock> stocks =
			analyzeRankingStock.analyzeRankingStock();

		stocks.stream().forEach(stock -> {
			System.out.println("stock = " + stock);
			gptStockAnalyzeService.analyzeStockNewsByDateWithStockName(LocalDate.now().minusYears(1), stock.getName());
		});


	}
}
