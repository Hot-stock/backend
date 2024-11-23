package com.bjcareer.GPTService.domain.gpt.insight;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KeyDateVariableDomain {
	private LocalDate date;
	private String reason;

	public KeyDateVariableDomain(LocalDate date, String reason) {
		this.date = date;
		this.reason = reason;
	}
}
