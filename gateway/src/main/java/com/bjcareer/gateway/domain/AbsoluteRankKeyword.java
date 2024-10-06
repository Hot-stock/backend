package com.bjcareer.gateway.domain;

import lombok.Data;

@Data
public class AbsoluteRankKeyword {
	private final Long absoluteKeywordCount;
	private final String period;

	public AbsoluteRankKeyword(Double absoluteKeyword, String period) {
		this.absoluteKeywordCount = absoluteKeyword.longValue();
		this.period = period;
	}
}
