package com.bjcareer.search.application.port.out.persistence.stockChart;

import com.bjcareer.search.domain.entity.StockChart;

public interface StockChartRepositoryPort {
	StockChart findChartByDate(LoadChartSpecificDateCommand command);

	StockChart findOhlcAboveThreshold(LoadChartAboveThresholdCommand command);

	void updateStockChartOfOHLC(StockChart stockChart);

	void save(StockChart stockChart);
}
