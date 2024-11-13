package com.bjcareer.search.domain.gpt.insight;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GPTInsight {
	private BuyRecommendationVariableDomain buyRecommendationVariableDomain;
	private String marketDrivers;
	private List<KeyDateVariableDomain> keyDateVariableDomains;
	private List<ShortTermStrategyVariableDomain> shortTermStrategyVariableDomains;
	private List<LongTermThesisVariableDomain> longTermThesisVariableDomains;
	private String volumeTargetForGain;
	private String riskPeriods;

	public GPTInsight(BuyRecommendationVariableDomain buyRecommendationVariableDomain, String marketDrivers,
		List<KeyDateVariableDomain> keyDateVariableDomains,
		List<ShortTermStrategyVariableDomain> shortTermStrategyVariableDomains,
		List<LongTermThesisVariableDomain> longTermThesisVariableDomains, String volumeTargetForGain,
		String riskPeriods) {
		this.buyRecommendationVariableDomain = buyRecommendationVariableDomain;
		this.marketDrivers = marketDrivers;
		this.keyDateVariableDomains = keyDateVariableDomains;
		this.shortTermStrategyVariableDomains = shortTermStrategyVariableDomains;
		this.longTermThesisVariableDomains = longTermThesisVariableDomains;
		this.volumeTargetForGain = volumeTargetForGain;
		this.riskPeriods = riskPeriods;
	}
}
