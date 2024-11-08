package com.bjcareer.search.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StockTest {

	@Test
	void 새로운_정보들로_업데이트_되는지_테스트(){
		Stock stock = new Stock("01234", "배럴", Market.KOSDAQ, "", 1000, 1000);
		Stock updateStock = new Stock("01234", "배럴", Market.KOSDAQ, "", 2000, 2000);

		stock.updateStockInfo(updateStock);

		assertEquals(updateStock.getIssuedShares(), stock.getIssuedShares());
		assertEquals(updateStock.getMarketCapitalization(), stock.getMarketCapitalization());
	}
  
}
