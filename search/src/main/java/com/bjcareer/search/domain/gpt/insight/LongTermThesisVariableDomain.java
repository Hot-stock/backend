package com.bjcareer.search.domain.gpt.insight;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LongTermThesisVariableDomain {
	// Historical Patterns: 장기적 패턴 및 유사한 과거 사건
	private List<HistoricalPatternDetail> historicalPatterns;

	// Catalyst Analysis: 촉매 분석
	private List<CatalystDetail> catalystAnalysis;

	// Risk and Resilience Factors: 장기 리스크와 회복 가능성 분석
	private String riskResilienceFactors;

	// Projected Outcomes: 장기 예측 결과
	private String projectedOutcomes;

	public LongTermThesisVariableDomain(List<HistoricalPatternDetail> historicalPatterns,
		List<CatalystDetail> catalystAnalysis, String riskResilienceFactors, String projectedOutcomes) {
		this.historicalPatterns = historicalPatterns;
		this.catalystAnalysis = catalystAnalysis;
		this.riskResilienceFactors = riskResilienceFactors;
		this.projectedOutcomes = projectedOutcomes;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class HistoricalPatternDetail {
		// Event Date: 과거 사건의 날짜
		private LocalDate eventDate;

		// Pattern Description: 과거 사건의 특성과 현재와의 유사성
		private String patternDescription;

		// Market Impact: 주가에 미친 영향
		private String marketImpact;

		// Investor Action: 슈퍼개미의 통찰과 행동
		private String investorAction;

		public HistoricalPatternDetail(LocalDate eventDate, String patternDescription, String marketImpact,
			String investorAction) {
			this.eventDate = eventDate;
			this.patternDescription = patternDescription;
			this.marketImpact = marketImpact;
			this.investorAction = investorAction;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CatalystDetail {
		// Catalyst Description: 촉매 설명
		private String catalystDescription;

		// Expected Impact: 예상되는 장기 영향
		private String expectedImpact;

		// Historical Comparison: 과거의 유사 촉매와 비교
		private String historicalComparison;

		// Investor Insight: 슈퍼개미의 통찰력
		private String investorInsight;

		public CatalystDetail(String catalystDescription, String expectedImpact, String historicalComparison,
			String investorInsight) {
			this.catalystDescription = catalystDescription;
			this.expectedImpact = expectedImpact;
			this.historicalComparison = historicalComparison;
			this.investorInsight = investorInsight;
		}
	}
}
