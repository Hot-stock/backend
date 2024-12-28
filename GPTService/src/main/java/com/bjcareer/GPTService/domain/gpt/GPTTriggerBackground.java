package com.bjcareer.GPTService.domain.gpt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

@Getter
@Document(collection = "background")  // MongoDB의 "news" 컬렉션에 매핑
public class GPTTriggerBackground {
	private final String thema;
	private final String nextUse;
	private final String nextUseReason;
	private final List<String> reasons = new ArrayList<>();
	private final Set<String> keywords = new HashSet<>();
	private final Set<String> stocks = new HashSet<>();

	public GPTTriggerBackground(String thema, String nextUse, String nextUseReason) {
		this.thema = thema;
		this.nextUse = nextUse;
		this.nextUseReason = nextUseReason;
	}

	public void addBackground(String reason, List<String> keywords) {
		reasons.add(reason);
		this.keywords.addAll(keywords);
	}
}
