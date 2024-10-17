package com.bjcareer.search.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bjcareer.search.application.stock.StockService;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.out.crawling.naver.CrawlingNaverFinance;
import com.bjcareer.search.repository.stock.StockRepository;
import com.bjcareer.search.application.exceptions.InvalidStockInformation;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

	public static final String STOCK_CODE = "12345";
	public static final String STOCK_NAME = "Stock1";
	public static final String THEMA = "Thema1";

	@Mock
	private StockRepository stockRepository;

	@Mock
	private CrawlingNaverFinance crawlingNaverFinance;

	@InjectMocks
	private StockService stockService;

	private Stock stock;

	@BeforeEach
	void setUpTestObjects() {
		stock = new Stock(STOCK_CODE, STOCK_NAME);
	}

	@Test
	void whenInvalidInformationIsAdded() {
		String wrongCode = "00000";

		when(stockRepository.findByCode(wrongCode)).thenReturn(Optional.empty());
		when(crawlingNaverFinance.getStock(wrongCode, STOCK_NAME)).thenReturn(stock);
		// Then
		assertThrows(InvalidStockInformation.class, () -> stockService.addStockThema(wrongCode, STOCK_NAME, THEMA));
	}
}
