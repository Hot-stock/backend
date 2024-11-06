package com.bjcareer.search.application.port.out.persistence.stockChart;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadChartSpecificDateCommand {
	private final String stockName;
	private final LocalDate date;
}
