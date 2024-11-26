package com.bjcareer.GPTService.domain.gpt;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
	private String stockName;
	private String reason;
	private String nextReasonFact;
	private String nextReasonOption;
	private LocalDate next;
	private OriginalNews news;
	@MongoId
	private String link;

	public GPTNewsDomain(String stockName, String reason, String next, String nextReasonFact, String nextReasonOption, OriginalNews news, boolean isRelated) {
		this.isRelated = isRelated;
		this.stockName = stockName;
		this.reason = reason;
		this.nextReasonFact = nextReasonFact;
		this.nextReasonOption = nextReasonOption;
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

	public Optional<LocalDate> getNext() {
		return Optional.ofNullable(next);
	}
}
