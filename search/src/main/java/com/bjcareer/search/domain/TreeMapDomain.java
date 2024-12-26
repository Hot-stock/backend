package com.bjcareer.search.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TreeMapDomain {
	private String themaName;
	private Map<String, Double> stockIncreaseRate = new HashMap<>();
	private Double value;

	public TreeMapDomain(ThemaInfo themaInfo, Map<Stock, StockChart> chart, int performance) {
		this.themaName = themaInfo.getName();
		this.value = sortByUpperRate(chart, performance);
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
		stockIncreaseRate.put(stock.getName(), stockAvg);
		return stockAvg;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TreeMapDomain that))
			return false;
		return Objects.equals(themaName, that.themaName);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(themaName);
	}
}
