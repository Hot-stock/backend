package com.bjcareer.GPTService.event.command;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AnalyzeStockEvent {
	private final String stockName;
	private final LocalDate startDate;
}
