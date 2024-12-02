package com.bjcareer.search.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.api.LoadStockInformationPort;
import com.bjcareer.search.application.port.out.api.StockChartQueryCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.persistence.stock.StockRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleOhlcService {
	private final LoadStockInformationPort apiServerPort;
	private final StockRepositoryAdapter stockRepository;
	private final StockChartRepositoryPort stockChartRepository;

	// @Scheduled(cron = "40 4 * * * *")
	@Transactional
	public void saveStockInfoAndChartData() {
		Map<String, Stock> stocks = loadEntities(stockRepository.findAll(), Stock::getCode);
		log.info("Stocks loaded: {}", stocks.size());
		updateStockInfo(stocks); // 정보 갱신 완료
		log.info("Renew Stocks Success: {}", stocks.size());

		// 가상 스레드를 사용한 ExecutorService 생성
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			stocks.values().stream()
				.map(stock -> executor.submit(() -> {
					Optional<StockChart> optStockChart = stockChartRepository.loadStockChart(stock.getCode());

					StockChart stockChart = optStockChart.orElseGet(() -> new StockChart(stock.getCode(), new ArrayList<>()));

					LocalDate startDay = stockChart.calculateStartDayForUpdateStockChart();
					StockChartQueryCommand stockChartQueryConfig = new StockChartQueryCommand(stock, startDay,
						LocalDate.now(AppConfig.ZONE_ID));

					log.debug("StockChartQueryConfig: {}", stockChartQueryConfig);
					stockChart.mergeOhlc(apiServerPort.loadStockChart(stockChartQueryConfig));

					return stock;
				}))
				.collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Error occurred while processing stocks in parallel", e);
		}

		stockRepository.saveALl(stocks.values());
		log.info("All Stocks was renewed: {}", stocks.size());
	}

	private void updateStockInfo(Map<String, Stock> stocks) {
		List<Stock> renewStocks = new ArrayList<>();

		renewStocks.addAll(apiServerPort.loadStockInfo(Market.KOSDAQ));
		renewStocks.addAll(apiServerPort.loadStockInfo(Market.KOSPI));

		for (Stock stock : renewStocks) {
			Stock pastStock = stocks.get(stock.getCode());

			if (pastStock != null) {
				pastStock.updateStockInfo(stock);
			} else {
				stocks.put(stock.getCode(), stock);
			}
		}
	}

	private <T, K> Map<K, T> loadEntities(List<T> entities, Function<T, K> keyMapper) {
		return entities.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
	}
}
