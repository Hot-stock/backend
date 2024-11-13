package com.bjcareer.search.application.port.in;

import java.time.LocalDate;

import lombok.Data;

@Data
public class GetInsightCommand {
	private String stockName;
	private LocalDate date;
}
