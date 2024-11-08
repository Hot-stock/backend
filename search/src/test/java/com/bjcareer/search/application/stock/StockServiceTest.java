package com.bjcareer.search.application.stock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.bjcareer.search.application.exceptions.InvalidStockInformationException;
import com.bjcareer.search.application.port.out.api.StockChartQueryCommand;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.out.persistence.repository.stock.StockRepository;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {
	@Mock
	private ApplicationEventPublisher publisher;

	@Mock
	private StockRepository stockRepository;

	@InjectMocks
	private StockService stockService;

	@Test
	void 없는_주식을_상대로_찿트_갱신을_요청함() {
		assertThrows(InvalidStockInformationException.class, () -> stockService.addStockChart("없는 주식"));
	}

	@Test
	void 데이터베이스에_저장된_주식의_차트_갱신을_요청함() {
		Stock stock = new Stock("017370", "우신시스템");

		when(stockRepository.findByName(stock.getName())).thenReturn(Optional.of(stock));

		stockService.addStockChart(stock.getName());

		verify(stockRepository, times(1)).findByName(stock.getName());

		// publisher.publishEvent(command);
		verify(publisher).publishEvent(any(StockChartQueryCommand.class));
	}
}
