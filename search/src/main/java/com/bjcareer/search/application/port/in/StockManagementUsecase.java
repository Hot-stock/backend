package com.bjcareer.search.application.port.in;

import com.bjcareer.search.domain.entity.Thema;

public interface StockManagementUsecase	 {
	Thema addStockThema(String stockCode, String stockName, String themeName);
	void addStockChart(String stockName);
}
