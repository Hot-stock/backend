package com.bjcareer.search.application.stock;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.exceptions.InvalidStockInformationException;
import com.bjcareer.search.application.port.in.StockManagementUsecase;
import com.bjcareer.search.application.port.out.api.StockChartQueryCommand;
import com.bjcareer.search.application.port.out.persistence.thema.LoadStockByThemaCommand;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.themaInfo.ThemaInfoRepositoryPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.persistence.repository.stock.StockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockService implements StockManagementUsecase {
	private final ApplicationEventPublisher publisher;
	private final ThemaRepositoryPort themaRepository;
	private final ThemaInfoRepositoryPort themaInfoRepository;
	private final StockRepository stockRepository;

	public Thema addStockThema(String stockCode, String stockName, String themeName) {
		Optional<Stock> byStockCode = stockRepository.findByCode(stockCode);
		Stock stock = byStockCode.orElseGet(() -> {
			log.info("Stock not found, information for stock: {} with code: {}", stockName, stockCode);

			throw new InvalidStockInformationException(
				"Invalid stock information provided for stock: " + stockName + " with code: " + stockCode);
		});

		Optional<ThemaInfo> byThemaName = themaInfoRepository.findByName(themeName);
		ThemaInfo themaInfo = byThemaName.orElseGet(
			() -> themaInfoRepository.save(new ThemaInfo(themeName, "USER CREATED")));

		LoadStockByThemaCommand command = new LoadStockByThemaCommand(stock.getName(), themaInfo.getName());
		return themaRepository.loadByStockNameAndThemaName(command)
			.orElseGet(() -> themaRepository.save(new Thema(stock, themaInfo)));
	}

	public void addStockChart(String stockName) {
		Optional<Stock> byStockCode = stockRepository.findByName(stockName);

		Stock stock = byStockCode.orElseGet(() -> {
			log.info("Stock not found, information for stock: {}", stockName);
			throw new InvalidStockInformationException(
				"Invalid stock information provided for stock with code: " + stockName);
		});

		StockChartQueryCommand command = new StockChartQueryCommand(stock, true);
		publisher.publishEvent(command);

	}
}
