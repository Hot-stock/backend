package com.bjcareer.gateway.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AbsoluteRankKeyword {
	private Long absoluteKeywordCount;
	private String period;

	public AbsoluteRankKeyword(Double absoluteKeyword, String period) {
		this.absoluteKeywordCount = absoluteKeyword.longValue();
		this.period = period;
	}

}
