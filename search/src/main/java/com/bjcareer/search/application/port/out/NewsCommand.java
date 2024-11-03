package com.bjcareer.search.application.port.out;

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

// http://52.79.156.30:5000/news?query=코로나&begin=2021-03-03&end=2021-03-03
