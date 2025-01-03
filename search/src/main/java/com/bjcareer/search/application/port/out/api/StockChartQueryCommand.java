package com.bjcareer.search.application.port.out.api;

import java.time.LocalDate;
import java.time.ZoneId;

import com.bjcareer.search.domain.entity.Stock;

import lombok.Getter;

@Getter
public class StockChartQueryCommand {
	private final Stock stock;
	private final String startDay;
	private final String endDay;
	ZoneId seoul = ZoneId.of("Asia/Seoul");

	public StockChartQueryCommand(Stock stock, LocalDate startDay, LocalDate endDay) {
		this.stock = stock;
		this.startDay = startDay.toString();
		this.endDay = endDay.toString();
	}

	public StockChartQueryCommand(Stock stock, boolean allData) {
		this.stock = stock;
		this.startDay = LocalDate.now().minusYears(1).toString();
		this.endDay = LocalDate.now(seoul).toString();
	}

	public String buildUrl(String baseUrl) {
		return String.format("%s?code=%s&startDay=%s&endDay=%s", baseUrl, stock.getCode(), startDay, endDay);
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
