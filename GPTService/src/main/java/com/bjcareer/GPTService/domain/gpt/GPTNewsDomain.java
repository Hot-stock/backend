package com.bjcareer.GPTService.domain.gpt;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.domain.gpt.thema.ThemaInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore
	private boolean isRelated;
	private String isRelatedDetail;
	@JsonIgnore
	private boolean isThema;
	private ThemaInfo themaInfo;
	@JsonIgnore
	private List<String> keywords;
	private String stockName;
	private String stockCode;
	private String reason;
	private String nextReasonFact;
	private String nextReasonOption;
	private LocalDate next;
	private OriginalNews news;
	@MongoId
	private String link;

	public GPTNewsDomain(String stockName, String reason, String next, String nextReasonFact,
		String nextReasonOption,
		OriginalNews news, boolean isRelated, String isRelatedDetail,  boolean isThema, List<String> keywords, ThemaInfo themaInfo) {
		this.isRelated = isRelated;
		this.isRelatedDetail = isRelatedDetail;
		this.stockName = stockName;
		this.reason = reason;
		this.nextReasonFact = nextReasonFact;
		this.nextReasonOption = nextReasonOption;
		this.isThema = isThema;
		this.news = news;
		this.link= news.getNewsLink();
		this.keywords = keywords;
		this.themaInfo = themaInfo;
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

	public Optional<LocalDate> getNext() {
		return Optional.ofNullable(next);
	}

	public void addStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		GPTNewsDomain that = (GPTNewsDomain)object;
		return isRelated == that.isRelated && Objects.equals(stockName, that.stockName)
			&& Objects.equals(reason, that.reason) && Objects.equals(nextReasonFact,
			that.nextReasonFact) && Objects.equals(nextReasonOption, that.nextReasonOption)
			&& Objects.equals(next, that.next) && Objects.equals(news, that.news)
			&& Objects.equals(link, that.link);
	}

	@Override
	public int hashCode() {
		return Objects.hash(isRelated, stockName, reason, nextReasonFact, nextReasonOption, next, news, link);
	}


}
