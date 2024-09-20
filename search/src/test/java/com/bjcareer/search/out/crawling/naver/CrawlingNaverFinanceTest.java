package com.bjcareer.search.out.crawling.naver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;

class CrawlingNaverFinanceTest {
	@Test
	void testExtractSpecificStock() {
		CrawlingNaverFinance mock = Mockito.mock(CrawlingNaverFinance.class);
		Stock stock = new Stock("267790", "Barrel", Market.KOSDAQ,
			"https://finance.naver.com/item/main.nhn?code=267790", 7888500L, 0L);

		when(mock.getStock("267790", "Barrel")).thenReturn(stock);

		verify(mock, times(1)).getStock("267790", "Barrel");
	}

	@Test
	void testInvalidInformationProvided() {
		CrawlingNaverFinance mock = Mockito.mock(CrawlingNaverFinance.class);
		Stock stock = new Stock("2677901", "Barrel", null, null, null, null);

		when(mock.getStock("2677901", "Barrel")).thenReturn(stock);
		Stock result = mock.getStock("2677901", "Barrel");

		assertEquals(false, result.validStock());
	}

}
