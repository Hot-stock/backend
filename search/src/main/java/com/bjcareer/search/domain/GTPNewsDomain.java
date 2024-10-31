package com.bjcareer.search.domain;

import java.time.LocalDate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class GTPNewsDomain {
	private final String stockName;
	private final String reason;
	private final String thema;
	private final LocalDate next;
	private final String nextReason;

	private News news;

	public GTPNewsDomain(String stockName, String reason, String thema, String next, String nextReason) {
		this.stockName = stockName;
		this.reason = reason;
		this.thema = thema;
		this.nextReason = nextReason;

		if (next == null || next.isEmpty()) {
			this.next = null;
		} else {
			this.next = LocalDate.parse(next);
		}
	}

	public void addNewsDomain(News news) {
		this.news = news;
	}
}
