package com.bjcareer.search.domain.gpt;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.bjcareer.search.domain.News;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class GTPNewsDomain {
	private String stockName;
	private String reason;
	private Map<String, String> themas;
	private String nextReason;
	private Optional<LocalDate> next;
	private News news;

	public GTPNewsDomain(String stockName, String reason, Map<String, String> themas, String next, String nextReason) {
		this.stockName = stockName;
		this.reason = reason;
		this.themas = themas;
		this.nextReason = nextReason;

		if (next == null || next.isEmpty()) {
			this.next = Optional.empty();
		} else {
			this.next = Optional.of(LocalDate.parse(next));
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
