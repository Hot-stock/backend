package com.bjcareer.search.application.port.out;

import java.util.List;

import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.api.python.StockChartQueryConfig;

public interface QueryStockServerPort {
	StockChart loadStockChart(StockChartQueryConfig config);

	List<Stock> loadStockInfo(Market market);
}
