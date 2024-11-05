package com.bjcareer.search.application.stock;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.exceptions.InvalidStockInformation;
import com.bjcareer.search.application.port.in.AddStockUsecase;
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
public class StockService implements AddStockUsecase {
	private final ThemaRepositoryPort themaRepository;
	private final ThemaInfoRepositoryPort themaInfoRepository;
	private final StockRepository stockRepository;


	public Thema addStockThema(String stockCode, String stockName, String themeName) {
		Optional<Stock> byStockCode = stockRepository.findByCode(stockCode);
		Stock stock = byStockCode.orElseGet(() -> {
			log.info("Stock not found, information for stock: {} with code: {}", stockName, stockCode);

			throw new InvalidStockInformation(
				"Invalid stock information provided for stock: " + stockName + " with code: " + stockCode);
		});

		Optional<ThemaInfo> byThemaName = themaInfoRepository.findByName(themeName);
		ThemaInfo themaInfo = byThemaName.orElseGet(
			() -> themaInfoRepository.save(new ThemaInfo(themeName, "USER CREATED")));

		LoadStockByThemaCommand command = new LoadStockByThemaCommand(stock.getName(), themaInfo.getName());
		return themaRepository.loadByStockNameAndThemaName(command)
			.orElseGet(() -> themaRepository.save(new Thema(stock, themaInfo)));
	}
}
