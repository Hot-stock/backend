package com.bjcareer.GPTService.domain.gpt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Getter;

@Getter
@Document(collection = "background")  // MongoDB의 "news" 컬렉션에 매핑
public class GPTTriggerBackground {
	@MongoId
	private final String thema;
	private final String background;
	private final String nextUse;
	private final String nextUseReason;
	private final Set<String> keywords = new HashSet<>();
	private final Set<String> stocks = new HashSet<>();

	public GPTTriggerBackground(String thema, String nextUse, String nextUseReason, String background) {
		this.thema = thema;
		this.nextUse = nextUse;
		this.nextUseReason = nextUseReason;
		this.background = background;
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
