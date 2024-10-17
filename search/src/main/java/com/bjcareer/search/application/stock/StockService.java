package com.bjcareer.search.application.stock;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.in.AddStockUsecase;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.crawling.naver.CrawlingNaverFinance;
import com.bjcareer.search.out.repository.stock.StockRepository;
import com.bjcareer.search.out.repository.stock.ThemaInfoRepository;
import com.bjcareer.search.out.repository.stock.ThemaRepository;
import com.bjcareer.search.application.exceptions.InvalidStockInformation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockService implements AddStockUsecase {
	private final ThemaRepository themaRepository;
	private final ThemaInfoRepository themaInfoRepository;
	private final StockRepository stockRepository;
	private final CrawlingNaverFinance crawlingNaverFinance;


	public Thema addStockThema(String stockCode, String stockName, String themeName) {
		Optional<Stock> byStockCode = stockRepository.findByCode(stockCode);
		Stock stock = byStockCode.orElseGet(() -> {
			log.info("Stock not found, crawling stock information for stock: {} with code: {}", stockName, stockCode);
			Stock result = crawlingNaverFinance.getStock(stockCode, stockName);

			if (stockCode.equals(result.getCode()) && stockName.equals(result.getName()) && result.validStock()) {
				return stockRepository.save(result);
			}

			throw new InvalidStockInformation(
				"Invalid stock information provided for stock: " + stockName + " with code: " + stockCode);
		});

		Optional<ThemaInfo> byThemaName = themaInfoRepository.findByName(themeName);
		ThemaInfo themaInfo = byThemaName.orElseGet(
			() -> themaInfoRepository.save(new ThemaInfo(themeName, "USER CREATED")));

		return themaRepository.findByStockNameAndThemaName(stock.getName(), themaInfo.getName())
			.orElseGet(() -> themaRepository.save(new Thema(stock, themaInfo)));
	}
}
