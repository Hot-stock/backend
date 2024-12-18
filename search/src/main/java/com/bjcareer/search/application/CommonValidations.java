package com.bjcareer.search.application;

import java.util.Optional;

import com.bjcareer.search.application.exceptions.InvalidStockInformationException;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

public class CommonValidations {
	public static Stock validationStock(StockRepositoryPort port, String stockName) {
		Optional<Stock> optStock = port.findByName(stockName);
		return optStock.orElseThrow(() -> new InvalidStockInformationException("찾아진 주식이 없습니다. 주식명을 확인해주세요!"));
	}

	public static StockChart validationStockChart(StockChartRepositoryPort port, String stockCode) {
		Optional<StockChart> stockChart = port.loadStockChart(stockCode);
		return stockChart.orElseThrow(
			() -> new InvalidStockInformationException("요청된 주식에 차트데이터가 없습니다. 차트 데이터 요청 버튼을 클릭해주세요!"));
	}

	// public static ThemaInfo validationThemaInfo(String Thema, String themaName) {
	// 	Optional<ThemaInfo> optThema = port.findThemaByName(themaName);
	// 	return optThema.orElseThrow(() -> new InvalidStockInformationException("찾아진 테마가 없습니다. 테마명을 확인해주세요!"));
	// }
}
