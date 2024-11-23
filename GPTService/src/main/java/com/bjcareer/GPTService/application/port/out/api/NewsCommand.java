package com.bjcareer.GPTService.application.port.out.api;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NewsCommand {
	private final String keyword;
	private final LocalDate startDate;
	private final LocalDate endDate;

	public String buildUrl(String baseUrl) {
		return String.format("%s?query=%s&begin=%s&end=%s", baseUrl, keyword, startDate, endDate);
	}
}
