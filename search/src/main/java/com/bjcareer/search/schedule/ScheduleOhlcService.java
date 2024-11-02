package com.bjcareer.search.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bjcareer.search.application.port.out.QueryStockServerPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.api.python.StockChartQueryConfig;
import com.bjcareer.search.out.persistence.repository.stock.StockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleOhlcService {
	private final QueryStockServerPort apiServerPort;
	private final StockRepository stockRepository;

	// @Scheduled(fixedDelay = 10000)
	public void saveStockInfoAndChartData() {
		Map<String, Stock> stocks = loadEntities(stockRepository.findAll(), Stock::getCode);
		log.info("Stocks loaded: {}", stocks.size());
		updateStockInfo(stocks); // 정보 갱신 완료
		log.info("Renew Stocks Success: {}", stocks.size());

		// 가상 스레드를 사용한 ExecutorService 생성
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			stocks.values().stream()
				.map(stock -> executor.submit(() -> {
					LocalDate startDay = stock.calculateStartDayForUpdateStockChart();
					StockChartQueryConfig stockChartQueryConfig = new StockChartQueryConfig(stock, startDay,
						LocalDate.now(AppConfig.ZONE_ID));

					log.debug("StockChartQueryConfig: {}", stockChartQueryConfig);
					StockChart stockChart = apiServerPort.loadStockChart(stockChartQueryConfig);
					stock.mergeStockChart(stockChart);

					return stock;
				}))
				.collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Error occurred while processing stocks in parallel", e);
		}

		stockRepository.saveAll(stocks.values());
		log.info("All Stocks was renewed: {}", stocks.size());
	}

	private void updateStockInfo(Map<String, Stock> stocks) {
		List<Stock> renewStocks = new ArrayList<>();

		renewStocks.addAll(apiServerPort.loadStockInfo(Market.KOSDAQ));
		renewStocks.addAll(apiServerPort.loadStockInfo(Market.KOSPI));

		for (Stock stock : renewStocks) {
			stocks.put(stock.getCode(), stock);
		}
	}

	private <T, K> Map<K, T> loadEntities(List<T> entities, Function<T, K> keyMapper) {
		return entities.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
	}
}
