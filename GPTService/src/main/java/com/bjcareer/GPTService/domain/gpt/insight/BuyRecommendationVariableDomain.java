package com.bjcareer.GPTService.domain.gpt.insight;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BuyRecommendationVariableDomain {
	private Integer score;
	private String reason;

	public BuyRecommendationVariableDomain(Integer score, String reason) {
		this.score = score;
		this.reason = reason;
	}
}
