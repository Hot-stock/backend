package com.bjcareer.search.application.port.out.persistence.stockChart;

import java.util.List;
import java.util.Optional;

import com.bjcareer.search.domain.entity.StockChart;

public interface StockChartRepositoryPort {
	StockChart findChartByDate(LoadChartSpecificDateCommand command);

	StockChart findOhlcAboveThreshold(LoadChartAboveThresholdCommand command);

	void updateStockChartOfOHLC(StockChart stockChart);

	void save(StockChart stockChart);
	void saveAll(List<StockChart> stockChart);

	Optional<StockChart> loadStockChart(String stockCode);
	List<StockChart> loadStockChartInStockCode(List<String> stockCode);
}
