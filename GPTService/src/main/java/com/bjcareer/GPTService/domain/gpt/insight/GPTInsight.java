package com.bjcareer.GPTService.domain.gpt.insight;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Document(collection = "insight")  // MongoDB의 "news" 컬렉션에 매핑
@NoArgsConstructor
public class GPTInsight {
	private boolean isFound;
	private String stockName;
	private String reason;
	private String reasonDetail;
	private Integer score;
	private LocalDate createdDate;

	public GPTInsight(String stockName, boolean isFound, String reason, String reasonDetail, Integer score) {
		this.stockName = stockName;
		this.isFound = isFound;
		this.reason = reason;
		this.reasonDetail = reasonDetail;
		this.score = score;
		this.createdDate = LocalDate.now();
	}
}
