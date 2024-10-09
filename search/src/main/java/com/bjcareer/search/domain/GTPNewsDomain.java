package com.bjcareer.search.domain;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class GTPNewsDomain {
	private final String stockName;
	private final String reason;
	private final String thema;
	private final LocalDate next;
	private final String next_reason;

	public GTPNewsDomain(String stockName, String reason, String thema, String next, String next_reason) {
		this.stockName = stockName;
		this.reason = reason;
		this.thema = thema;
		this.next_reason = next_reason;
		this.next = LocalDate.parse(next);
	}
}
