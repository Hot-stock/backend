package com.bjcareer.search.out.api.python;

import java.time.LocalDate;
import java.time.ZoneId;

import com.bjcareer.search.domain.entity.Stock;

import lombok.Getter;

@Getter
public class StockChartQueryConfig {
	private final Stock stock;
	private final String startDay;
	private final String endDay;
	ZoneId seoul = ZoneId.of("Asia/Seoul");

	public StockChartQueryConfig(Stock stock, LocalDate startDay, LocalDate endDay) {
		this.stock = stock;
		this.startDay = startDay.toString();
		this.endDay = endDay.toString();
	}

	public StockChartQueryConfig(Stock stock, boolean allData) {
		this.stock = stock;
		this.startDay = "1999-01-01";
		this.endDay = LocalDate.now(seoul).toString();
	}

	public String buildUrl(String baseUrl) {
		return String.format("%s?code=%s&'startDay'=%s&'endDay'=%s", baseUrl, stock.getCode(), startDay, endDay);
	}

	@Override
	public String toString() {
		return "StockChartQueryConfig{" +
			"stock=" + stock.getName() +
			", startDay='" + startDay + '\'' +
			", endDay='" + endDay + '\'' +
			", seoul=" + seoul +
			'}';
	}
}
