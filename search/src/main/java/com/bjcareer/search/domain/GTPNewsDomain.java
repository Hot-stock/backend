package com.bjcareer.search.domain;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString
public class GTPNewsDomain {
	private final String stockName;
	private final String reason;
	private final String thema;
	private final String nextReason;
	private final LocalDate next;
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

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		GTPNewsDomain that = (GTPNewsDomain)object;
		return Objects.equals(stockName, that.stockName) && Objects.equals(news, that.news);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stockName, news);
	}
}
