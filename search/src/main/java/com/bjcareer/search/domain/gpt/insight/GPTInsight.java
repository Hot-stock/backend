package com.bjcareer.search.domain.gpt.insight;

import java.time.LocalDate;

import com.bjcareer.search.domain.entity.Stock;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class GPTInsight {
	private final boolean isFound;
	private final String stockName;
	private final String reason;
	private final String reasonDetail;
	private final Integer score;
	private final LocalDate createdAt;
	private Stock stock;

	public void addStock(Stock stock) {
		this.stock = stock;
	}
}
