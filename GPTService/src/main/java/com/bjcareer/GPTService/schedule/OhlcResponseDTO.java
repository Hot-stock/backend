package com.bjcareer.GPTService.schedule;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OhlcResponseDTO {
	private LocalDate date;
	private int open;
	private int high;
	private int low;
	private int close;
	private Long volume;

	@JsonProperty("change")
	private int percentageIncrease;
}


