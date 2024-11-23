package com.bjcareer.GPTService.in.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AnalyzeStockNewsRequestDTO {
	LocalDate date;
	String stockName;
}
