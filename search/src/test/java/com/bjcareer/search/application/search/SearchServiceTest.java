package com.bjcareer.search.application.search;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.exceptions.InvalidStockInformation;
import com.bjcareer.search.application.stock.StockService;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.out.persistence.repository.stock.StockRepository;

@SpringBootTest
class SearchServiceTest {
	@Autowired
	SearchService searchService;

	@Test
	void 주어진_키워드로_검색결과를_반환하는지() {
		//given
		String keyword = "중국";
		int page = 0;
		int size = 10;

		//when
		List<Thema> searchResult = searchService.getSearchResult(keyword, page, size);

		//then
		assertFalse(searchResult.isEmpty());
	}

	@ExtendWith(MockitoExtension.class)
	static
	class StockServiceMockTest {
		public static final String STOCK_NAME = "Stock1";
		public static final String THEMA = "Thema1";

		@Mock
		private StockRepository stockRepository;

		@InjectMocks
		private StockService stockService;

		@Test
		void whenInvalidInformationIsAdded() {
			String wrongCode = "00000";

			when(stockRepository.findByCode(wrongCode)).thenReturn(Optional.empty());
			// Then
			assertThrows(InvalidStockInformation.class, () -> stockService.addStockThema(wrongCode, STOCK_NAME, THEMA));
		}
	}
}
