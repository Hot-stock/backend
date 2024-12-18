package com.bjcareer.search.domain;

import java.util.HashMap;
import java.util.Map;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.Getter;

@Getter
public class TreeMapDomain {
	private final ThemaInfo themaInfo;
	private final Map<Stock, Double> stockIncreaseRate = new HashMap<>();
	private final Double increaseRate;

	public TreeMapDomain(ThemaInfo themaInfo, Map<Stock, StockChart> chart, int performance) {
		this.themaInfo = themaInfo;
		this.increaseRate = sortByUpperRate(chart, performance);
	}

	private Double sortByUpperRate(Map<Stock, StockChart> chart, int day) {
		Double totalAvg = 0.0;
		for (Stock stock : chart.keySet()) {
			StockChart stockChart = chart.get(stock);

			Double stockAvg = setStockRaiseRate(day, stock, stockChart);

			totalAvg += stockAvg;
		}

		if (stockIncreaseRate.isEmpty()) {
			return 0.0;
		}

		return totalAvg / stockIncreaseRate.size();
	}

	private Double setStockRaiseRate(int performance, Stock stock, StockChart stockChart) {
		Double stockAvg = stockChart.calcMovingAverageOfIncrease(performance);
		stockIncreaseRate.put(stock, stockAvg);
		return stockAvg;
	}
}
