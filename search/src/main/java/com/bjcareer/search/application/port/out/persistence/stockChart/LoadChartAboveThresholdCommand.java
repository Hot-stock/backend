package com.bjcareer.search.application.port.out.persistence.stockChart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadChartAboveThresholdCommand {
	private final String code;
	private final int threshold;
}
