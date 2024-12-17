package com.bjcareer.GPTService.schedule;

import java.time.LocalDate;
import java.time.ZoneId;

import lombok.Getter;

@Getter
public class StockChartQueryCommand {
	private final String code;
	private final String startDay;
	private final String endDay;
	ZoneId seoul = ZoneId.of("Asia/Seoul");

	public StockChartQueryCommand(String code, LocalDate startDay, LocalDate endDay) {
		this.code = code;
		this.startDay = startDay.toString();
		this.endDay = endDay.toString();
	}

	public StockChartQueryCommand(String code, boolean allData) {
		this.code = code;
		this.startDay = "2005-01-01";
		this.endDay = LocalDate.now(seoul).toString();
	}

	public String buildUrl(String baseUrl) {
		return String.format("%s?code=%s&startDay=%s&endDay=%s", baseUrl, this.code, startDay, endDay);
	}

	@Override
	public String toString() {
		return "StockChartQueryConfig{" +
			"stock=" + code +
			", startDay='" + startDay + '\'' +
			", endDay='" + endDay + '\'' +
			", seoul=" + seoul +
			'}';
	}
}
