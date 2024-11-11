package com.bjcareer.search.domain.gpt.thema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class GPTThema {
	private String summary;
	private List<CatalystsVariableDomain> catalysts;
	private String policyImpactAnalysis;
	private String recoveryProjectDetails;
	private String interestRateImpact;
	private String marketOutlook;
	private String scenarioAnalysis;
	private String keyUpcomingDates;
	private String investmentAttractiveness;

	public GPTThema(String summary, List<CatalystsVariableDomain> catalysts, String policyImpactAnalysis,
		String recoveryProjectDetails,
		String interestRateImpact, String marketOutlook, String scenarioAnalysis, String keyUpcomingDates,
		String investmentAttractiveness) {
		this.summary = summary;
		this.catalysts = catalysts;
		this.policyImpactAnalysis = policyImpactAnalysis;
		this.recoveryProjectDetails = recoveryProjectDetails;
		this.interestRateImpact = interestRateImpact;
		this.marketOutlook = marketOutlook;
		this.scenarioAnalysis = scenarioAnalysis;
		this.keyUpcomingDates = keyUpcomingDates;
		this.investmentAttractiveness = investmentAttractiveness;
	}
}
