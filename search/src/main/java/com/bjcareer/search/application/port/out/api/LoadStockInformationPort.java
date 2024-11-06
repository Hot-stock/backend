package com.bjcareer.search.application.port.out.api;

import java.util.List;

import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

public interface LoadStockInformationPort {
	StockChart loadStockChart(StockChartQueryCommand command);
	List<Stock> loadStockInfo(Market market);
}
