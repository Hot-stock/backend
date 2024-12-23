package com.bjcareer.search.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.api.LoadStockInformationPort;
import com.bjcareer.search.application.port.out.api.StockChartQueryCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.event.messages.UpdateStockLogoMessage;
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
	private final ApplicationEventPublisher eventPublisher;

	// @Scheduled(fixedDelay = 300000)
	@Transactional(readOnly = true)
	public void saveStockInfoAndChartData() {
		Map<String, Stock> stocks = loadEntities(stockRepository.findAll(), Stock::getCode);
		updateStockInfo(stocks);
		log.info("Renew Stocks Success: {}", stocks.size());

		List<StockChart> stockCharts = stockChartRepository.loadStockChartInStockCode(
			stocks.values().stream().map(Stock::getCode).toList());

		for (StockChart chart : stockCharts) {
			log.info("Renew Stock Chart: {}", chart.getStockCode());
			Stock stock = stocks.get(chart.getStockCode());
			StockChartQueryCommand stockChartQueryConfig = new StockChartQueryCommand(stock,
				chart.getLastUpdateDate(),
				LocalDate.now(AppConfig.ZONE_ID)); //1분
			chart.mergeOhlc(apiServerPort.loadStockChart(stockChartQueryConfig));
		}

		stockChartRepository.saveAll(stockCharts);
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
				eventPublisher.publishEvent(new UpdateStockLogoMessage(stock));
				stocks.put(stock.getCode(), stock);
			}
		}
	}

	private <T, K> Map<K, T> loadEntities(List<T> entities, Function<T, K> keyMapper) {
		return entities.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
	}
}
