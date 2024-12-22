package com.bjcareer.GPTService.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.config.AppConfig;
import com.bjcareer.GPTService.domain.Stock;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.rdb.StockRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisTrendKeywordRankAdapter;
import com.bjcareer.GPTService.schedule.OhlcResponseDTO;
import com.bjcareer.GPTService.schedule.StockChartQueryCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyzeRankingStock {
	private final RedisTrendKeywordRankAdapter redisTrendKeywordRankAdapter;
	private final PythonSearchServerAdapter pythonSearchServerAdapter;
	private final StockRepository stockRepository;
	private final GPTStockAnalyzeService gptStockAnalyzeService;

	@Qualifier("customTaskExecutor")
	private final Executor executor;

	public List<Stock> analyzeRankingStock() {
		LocalDate startDate = LocalDate.now().minusYears(1);
		LocalDate endDate = LocalDate.now(AppConfig.ZONE_ID);

		List<String> ranking = redisTrendKeywordRankAdapter.getRanking(20);

		// 비동기 작업 수행
		List<CompletableFuture<Optional<Stock>>> futures = ranking.stream()
			.map(keyword -> CompletableFuture.supplyAsync(() -> processKeyword(keyword, startDate, endDate), executor))
			.toList();

		// 비동기 작업 완료 후 결과 수집

		return futures.stream()
			.map(CompletableFuture::join) // 비동기 작업 완료 대기
			.filter(Optional::isPresent) // 결과가 있는지 확인
			.map(Optional::get) // Optional에서 값 추출
			.collect(Collectors.toList());
	}

	private Optional<Stock> processKeyword(String keyword, LocalDate startDate, LocalDate endDate) {
		Optional<Stock> byName = stockRepository.findByName(keyword);
		if (byName.isEmpty()) {
			return Optional.empty();
		}

		Stock stock = byName.get();
		long maxMarketCap = 120000000000L;
		long marketCap = (long)stock.getIssuedShares() * (long)stock.getPrice();
		if (maxMarketCap < marketCap) {
			return Optional.empty();
		}

		List<OhlcResponseDTO> ohlcResponseDTOS = pythonSearchServerAdapter.loadStockChart(
			new StockChartQueryCommand(stock.getCode(), startDate, endDate));

		int percentageIncrease = ohlcResponseDTOS.get(ohlcResponseDTOS.size() - 2).getPercentageIncrease();
		if (7 <= percentageIncrease) {
			return Optional.empty();
		}

		gptStockAnalyzeService.analyzeStockNewsByDateWithStockName(startDate, stock.getName());
		return Optional.of(stock);
	}

	public double calculateGrowthRate(int oldValue, int newValue) {
		if (oldValue == 0) {
			throw new IllegalArgumentException("기준 값은 0일 수 없습니다.");
		}
		return ((double)(newValue - oldValue) / oldValue) * 100;
	}

}
