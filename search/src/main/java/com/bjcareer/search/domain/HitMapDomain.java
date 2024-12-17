package com.bjcareer.search.domain;

import java.util.HashMap;
import java.util.Map;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.Getter;
import lombok.ToString;

@Getter
public class HitMapDomain {
	private final ThemaInfo themaInfo;
	private final Map<Stock, Integer> stockIncreaseRate = new HashMap<>();
	private final Integer increaseRate;

	public HitMapDomain(ThemaInfo themaInfo, Map<Stock, StockChart> chart, int avgDay) {
		this.themaInfo = themaInfo;
		this.increaseRate = sortByUpperRate(chart, avgDay);
	}

	private Integer sortByUpperRate(Map<Stock, StockChart> chart, int day) {
		Integer totalAvg = 0;
		for (Stock stock : chart.keySet()) {
			StockChart stockChart = chart.get(stock);

			Integer stockAvg = setStockRaiseRate(day, stock, stockChart);

			totalAvg += stockAvg;
		}

		if(stockIncreaseRate.isEmpty()) {
			return 0;
		}

		return totalAvg / stockIncreaseRate.size();
	}

	private Integer setStockRaiseRate(int day, Stock stock, StockChart stockChart) {
		Integer stockAvg = stockChart.calcMovingAverageOfIncrease(day);
		stockIncreaseRate.put(stock, stockAvg);
		return stockAvg;
	}

	@Override
	public String toString() {
		return "HitMapDomain{" +
				"themaName=" + themaInfo.getName() +
				", stockUpperRate=" + stockIncreaseRate.keySet().stream().map(Stock::getName).toList() +
				", upperRate=" + increaseRate +
				'}';
	}
}
