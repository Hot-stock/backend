package com.bjcareer.search.out.crawling.naver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;


class CrawlingThemaTest {

	@Test
	void test_특정_종목_추출() throws IOException {
		CrawlingThema crawlingThema = new CrawlingThema();
		Integer page = 1;
		Stock stock = crawlingThema.getStock("267790", "배럴");

		assertEquals(true, stock.getCode().equals("267790"));
		assertEquals(true, stock.getName().equals("배럴"));
		assertEquals(true, stock.getMarket() == Market.KOSDAQ);
		assertEquals(true, stock.getIssuedShares() == 7888500);
	}

}