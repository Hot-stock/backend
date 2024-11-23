package com.bjcareer.GPTService.domain.gpt;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Document(collection = "news")  // MongoDB의 "news" 컬렉션에 매핑
public class GPTNewsDomain {
	private String stockName;
	private String reason;
	private List<GPTThema> themas;
	private String nextReason;
	private LocalDate next;
	private OriginalNews news;
	@MongoId
	private String link;

	public GPTNewsDomain(String stockName, String reason, List<GPTThema> themas, String next, String nextReason, OriginalNews news) {
		this.stockName = stockName;
		this.reason = reason;
		this.themas = themas;
		this.nextReason = nextReason;
		this.news = news;
		this.link= news.getNewsLink();

		parseLocalDate(next);
	}

	private void parseLocalDate(String next) {
		if (next == null || next.isEmpty()) {
			this.next = null;
		} else {
			try {
				this.next = LocalDate.parse(next);
			} catch (Exception e) {
				log.error("Failed to parse date: {}", next);
				this.next = null;
			}
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		GPTNewsDomain that = (GPTNewsDomain)object;
		return Objects.equals(stockName, that.stockName) && Objects.equals(reason, that.reason)
			&& Objects.equals(themas, that.themas) && Objects.equals(nextReason, that.nextReason)
			&& Objects.equals(next, that.next) && Objects.equals(news, that.news)
			&& Objects.equals(link, that.link);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stockName, reason, themas, nextReason, next, news, link);
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

	public Optional<LocalDate> getNext() {
		return Optional.ofNullable(next);
	}
}
