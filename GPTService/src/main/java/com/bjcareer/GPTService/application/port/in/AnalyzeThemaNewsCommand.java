package com.bjcareer.GPTService.application.port.in;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalyzeThemaNewsCommand {
	private String keyword;
	private LocalDate date;

	public AnalyzeThemaNewsCommand(String keyword, LocalDate date) {
		this.keyword = keyword;
		this.date = date;
	}
}
