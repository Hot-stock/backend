package com.bjcareer.search.domain.gpt.insight;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShortTermStrategyVariableDomain {
	// Key Date: 전략의 핵심 날짜
	private LocalDate date;

	// Catalyst Impact: 촉매 영향 분석
	private String catalystImpact;

	// Historical Impact: 유사한 과거 사건이 미친 영향
	private List<HistoricalImpactDetail> historicalImpact;

	// Predicted Impact: 과거 경향에 따른 예상 영향
	private String predictedImpact;

	// Probability of Increase: 예상 상승 확률
	private String probabilityOfIncrease;

	// Investor Insight: 슈퍼개미의 통찰력
	private String investorInsight;

	public ShortTermStrategyVariableDomain(LocalDate date, String catalystImpact,
		List<HistoricalImpactDetail> historicalImpact, String predictedImpact, String probabilityOfIncrease,
		String investorInsight) {
		this.date = date;
		this.catalystImpact = catalystImpact;
		this.historicalImpact = historicalImpact;
		this.predictedImpact = predictedImpact;
		this.probabilityOfIncrease = probabilityOfIncrease;
		this.investorInsight = investorInsight;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class HistoricalImpactDetail {
		// Impact Date: 과거 이벤트 발생 날짜
		private LocalDate impactDate;

		// Reason for Rise: 주가 상승 이유
		private String reasonForRise;

		// Source Link: 출처 링크
		private String sourceLink;

		public HistoricalImpactDetail(LocalDate impactDate, String reasonForRise, String sourceLink) {
			this.impactDate = impactDate;
			this.reasonForRise = reasonForRise;
			this.sourceLink = sourceLink;
		}
	}
}
