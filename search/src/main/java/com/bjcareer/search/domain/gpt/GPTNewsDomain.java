package com.bjcareer.search.domain.gpt;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.bjcareer.search.domain.News;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class GPTNewsDomain {
	private String stockName;
	private String reason;
	private List<GPTThema> themas;
	private String nextReason;
	private Optional<LocalDate> next;
	private News news;

	public GPTNewsDomain(String stockName, String reason, List<GPTThema> themas, String next, String nextReason,
		News news) {
		this.stockName = stockName;
		this.reason = reason;
		this.themas = themas;
		this.nextReason = nextReason;
		this.news = news;

		parseLocalDate(next);
	}

	public GPTNewsDomain(String stockName, String reason, List<GPTThema> themas, String next, String nextReason) {
		this(stockName, reason, themas, next, nextReason, null);
	}

	private void parseLocalDate(String next) {
		if (next == null || next.isEmpty()) {
			this.next = Optional.empty();
		} else {
			try {
				this.next = Optional.of(LocalDate.parse(next));
			}catch (Exception e) {
				log.error("Failed to parse date: {}", next);
				this.next = Optional.empty();
			}
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
		GPTNewsDomain that = (GPTNewsDomain)object;
		return Objects.equals(stockName, that.stockName) && Objects.equals(news, that.news);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stockName, news);
	}

	@Data
	@NoArgsConstructor
	public static class GPTThema {
		private String name;
		private String reason;

		public GPTThema(String name, String reason) {
			this.name = name;
			this.reason = reason;
		}
	}
}
