package com.bjcareer.search.domain.gpt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class GPTStockNewsDomain {
	private String stockName;
	private String reason;
	private String stockCode;
	private List<String> keywords = new ArrayList<>();
	private String nextReason;
	private Optional<LocalDate> next;
	private News news;
	private String preSignedStockLogoUrl;
	private List<String> themas = new ArrayList<>();


	public GPTStockNewsDomain(String stockName, String stockCode,  String reason, List<String> keywords, String next, String nextReason,
		News news) {
		this.stockName = stockName;
		this.stockCode = stockCode;
		this.reason = reason;
		this.nextReason = nextReason;
		this.news = news;
		this.keywords = keywords;

		parseLocalDate(next);
	}

	public GPTStockNewsDomain(String stockName, String stockCode, String reason, List<String> keywords, String next, String nextReason) {
		this(stockName, stockCode, reason, keywords, next, nextReason, null);
	}


	public GPTStockNewsDomain(String stockName, String reason) {
		this(stockName, null, reason, new ArrayList<>(), null, null, null);
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

	public void addThema(List<String> thema) {
		this.themas = thema;
	}

	public void linkPreSignedStockLogoUrl(String preSignedStockLogoUrl) {
		this.preSignedStockLogoUrl = preSignedStockLogoUrl;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		GPTStockNewsDomain that = (GPTStockNewsDomain)object;
		return Objects.equals(stockName, that.stockName) && Objects.equals(news, that.news);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stockName, news);
	}
}
