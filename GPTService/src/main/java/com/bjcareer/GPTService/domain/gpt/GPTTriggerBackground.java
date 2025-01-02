package com.bjcareer.GPTService.domain.gpt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Document(collection = "background")  // MongoDB의 "news" 컬렉션에 매핑
@NoArgsConstructor
public class GPTTriggerBackground {
	@MongoId
	private String thema;
	private String background;
	private String nextUse;
	private String nextUseReason;
	private Set<String> keywords;
	private Set<String> stocks;


	public GPTTriggerBackground(String thema, String nextUse, String nextUseReason, String background) {
		this.thema = thema;
		this.nextUse = nextUse;
		this.nextUseReason = nextUseReason;
		this.background = background;
		this.keywords = new HashSet<>();
		this.stocks = new HashSet<>();
	}

	public void addKeywords(List<String> keywords) {
		this.keywords.addAll(keywords);
	}

	public boolean isNeedToUpdate(String stockName) {
		return !this.stocks.contains(stockName);
	}

	public void addStocks(List<String> stockNames) {
		this.stocks.addAll(stockNames);
	}
}
