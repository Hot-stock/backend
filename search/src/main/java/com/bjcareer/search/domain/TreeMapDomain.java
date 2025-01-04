package com.bjcareer.search.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		this.value = roundToTwoDecimalPlaces(sortByUpperRate(chart, performance));
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
		stockAvg = roundToTwoDecimalPlaces(stockAvg); // 소수점 두 자리로 반올림
		stockIncreaseRate.put(stock.getName(), stockAvg);
		return stockAvg;
	}

	private Double roundToTwoDecimalPlaces(Double value) {
		if (value == null) {
			return 0.0;
		}
		return BigDecimal.valueOf(value)
			.setScale(2, RoundingMode.HALF_UP)
			.doubleValue();
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
