package com.bjcareer.search.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.event.SearchedKeyword;
import com.bjcareer.search.out.crawling.naver.CrawlingNaverFinance;
import com.bjcareer.search.repository.stock.StockRepository;
import com.bjcareer.search.repository.stock.ThemaInfoRepository;
import com.bjcareer.search.repository.stock.ThemaRepository;
import com.bjcareer.search.service.exceptions.InvalidStockInformation;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

	public static final String STOCK_CODE = "12345";
	public static final String STOCK_NAME = "Stock1";
	public static final String THEMA = "Thema1";
	@Mock
	private ThemaRepository themaRepository;

	@Mock
	private ThemaInfoRepository themaInfoRepository;

	@Mock
	private StockRepository stockRepository;

	@Mock
	private CrawlingNaverFinance crawlingNaverFinance;

	@Mock
	private ApplicationEventPublisher eventListener;

	@InjectMocks
	private StockService stockService;

	private Stock stock;
	private ThemaInfo themaInfo;
	private Thema existingThema;

	@BeforeEach
	void setUpTestObjects() {
		stock = new Stock(STOCK_CODE, STOCK_NAME);
		themaInfo = new ThemaInfo(THEMA, "USER CREATED");
		existingThema = new Thema(stock, themaInfo);
	}

	@Test
	void testGetStockOfThema() {
		String query = "stock";
		// Given
		when(themaRepository.findByStockNameContaining(query)).thenReturn(Arrays.asList(existingThema));

		// When
		List<Thema> result = stockService.getStockOfThema(query);

		// Then
		assertEquals(1, result.size());
		verify(themaRepository, times(1)).findByStockNameContaining(query);
		verify(eventListener, times(1)).publishEvent(any(SearchedKeyword.class));
	}

	@Test
	void testAddStockThemaExistingStockAndThema() {
		when(stockRepository.findByName(STOCK_NAME)).thenReturn(Optional.of(stock));
		when(themaInfoRepository.findByThemaName(THEMA)).thenReturn(Optional.of(themaInfo));
		when(themaRepository.findByStockNameAndThemaName(STOCK_NAME, THEMA)).thenReturn(Optional.of(existingThema));

		// When
		Thema result = stockService.addStockThema(STOCK_CODE, STOCK_NAME, THEMA);

		// Then
		assertEquals(existingThema, result);
		verifyCommonRepositoryCalls();
	}

	@Test
	void testAddStockThemaNewStockAndNewThema() {
		// Given
		Thema newThema = new Thema(stock, themaInfo);

		when(stockRepository.findByName(STOCK_NAME)).thenReturn(Optional.empty());
		when(crawlingNaverFinance.getStock(STOCK_CODE, STOCK_NAME)).thenReturn(stock);
		when(stockRepository.save(stock)).thenReturn(stock);

		when(themaInfoRepository.findByThemaName(THEMA)).thenReturn(Optional.empty());
		when(themaInfoRepository.save(any(ThemaInfo.class))).thenReturn(themaInfo);
		when(themaRepository.findByStockNameAndThemaName(STOCK_NAME, THEMA)).thenReturn(Optional.empty());
		when(themaRepository.save(any(Thema.class))).thenReturn(newThema);

		// When
		Thema result = stockService.addStockThema(STOCK_CODE, STOCK_NAME, THEMA);

		// Then
		assertNotNull(result);
		assertEquals(newThema, result);
		verifyCommonRepositoryCalls();
		verify(crawlingNaverFinance, times(1)).getStock(STOCK_CODE, STOCK_NAME);
		verify(stockRepository, times(1)).save(stock);
		verify(themaInfoRepository, times(1)).save(any(ThemaInfo.class));
		verify(themaRepository, times(1)).save(any(Thema.class));
	}

	@Test
	void 잘못된_정보로_추가를_요청했을_떄() {
		// Given
		String INVALID_CODE = "00000";
		Stock stock = new Stock(INVALID_CODE, STOCK_NAME);

		when(stockRepository.findByName(STOCK_NAME)).thenReturn(Optional.empty());
		when(crawlingNaverFinance.getStock(INVALID_CODE, STOCK_NAME)).thenReturn(stock);

		// Then
		assertThrows(InvalidStockInformation.class, () -> stockService.addStockThema(INVALID_CODE, STOCK_NAME, THEMA));
	}

	// Common repository calls verification
	private void verifyCommonRepositoryCalls() {
		verify(stockRepository, times(1)).findByName(STOCK_NAME);
		verify(themaInfoRepository, times(1)).findByThemaName(THEMA);
		verify(themaRepository, times(1)).findByStockNameAndThemaName(STOCK_NAME, THEMA);
	}
}
