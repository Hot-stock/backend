package com.bjcareer.GPTService.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.config.AppConfig;
import com.bjcareer.GPTService.domain.Stock;
import com.bjcareer.GPTService.event.command.AnalyzeStockEvent;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.rdb.StockRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisSuggestionStock;
import com.bjcareer.GPTService.out.persistence.redis.RedisTrendKeywordRankAdapter;
import com.bjcareer.GPTService.schedule.OhlcResponseDTO;
import com.bjcareer.GPTService.schedule.StockChartQueryCommand;

@Service
public class AnalyzeRankingStock {
	public static final long MAX_MARKET_CAP = 120000000000L;
	private final RedisTrendKeywordRankAdapter redisTrendKeywordRankAdapter;
	private final PythonSearchServerAdapter pythonSearchServerAdapter;
	private final StockRepository stockRepository;
	private final RedisSuggestionStock redisSuggestionStock;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final Executor executor;

	public AnalyzeRankingStock(RedisTrendKeywordRankAdapter redisTrendKeywordRankAdapter,
		PythonSearchServerAdapter pythonSearchServerAdapter, StockRepository stockRepository,
		RedisSuggestionStock redisSuggestionStock, ApplicationEventPublisher applicationEventPublisher,
		@Qualifier("customTaskExecutor") Executor executor) {
		this.redisTrendKeywordRankAdapter = redisTrendKeywordRankAdapter;
		this.pythonSearchServerAdapter = pythonSearchServerAdapter;
		this.stockRepository = stockRepository;
		this.redisSuggestionStock = redisSuggestionStock;
		this.applicationEventPublisher = applicationEventPublisher;
		this.executor = executor;
	}

	public List<Stock> analyzeRankingStock() {
		LocalDate startDate = LocalDate.now().minusYears(1);
		LocalDate endDate = LocalDate.now(AppConfig.ZONE_ID);

		List<String> ranking = redisTrendKeywordRankAdapter.getRanking(20,
			RedisTrendKeywordRankAdapter.STOCK_RANK_BUCKET);

		// 비동기 작업 수행
		List<CompletableFuture<Optional<Stock>>> futures = ranking.stream()
			.map(keyword -> CompletableFuture.supplyAsync(() -> processKeyword(keyword, startDate, endDate), executor))
			.toList();

		return futures.stream()
			.map(CompletableFuture::join) // 비동기 작업 완료 대기
			.filter(Optional::isPresent) // 결과가 있는지 확인
			.map(Optional::get) // Optional에서 값 추출
			.map(stock -> {
				redisSuggestionStock.updateSuggestionStock(stock.getName());
				return stock;
			})
			.collect(Collectors.toList());
	}

	private Optional<Stock> processKeyword(String keyword, LocalDate startDate, LocalDate endDate) {
		Optional<Stock> byName = stockRepository.findByName(keyword);
		if (byName.isEmpty()) {
			return Optional.empty();
		}

		Stock stock = byName.get();
		applicationEventPublisher.publishEvent(new AnalyzeStockEvent(stock.getName(), startDate));

		long marketCap = (long)stock.getIssuedShares() * (long)stock.getPrice();

		if (MAX_MARKET_CAP < marketCap) {
			return Optional.empty();
		}

		List<OhlcResponseDTO> ohlcResponseDTOS = pythonSearchServerAdapter.loadStockChart(
			new StockChartQueryCommand(stock.getCode(), startDate, endDate));

		int yesterday = ohlcResponseDTOS.size() - 2;

		if (yesterday < 0) {
			return Optional.empty();
		}

		int percentageIncrease = ohlcResponseDTOS.get(yesterday).getPercentageIncrease();

		if (7 <= percentageIncrease) {
			return Optional.empty();
		}

		return Optional.of(stock);
	}

}
