package com.bjcareer.search.out.api.gpt.insight;

import java.time.LocalDate;
import java.util.List;

public class LongTermThesisVariableResponseDTO {
	// Historical Patterns: 장기적 패턴 및 유사한 과거 사건
	public List<HistoricalPatternDetail> historicalPatterns;

	// Catalyst Analysis: 촉매 분석
	public List<CatalystDetail> catalystAnalysis;

	// Risk and Resilience Factors: 장기 리스크와 회복 가능성 분석
	public String riskResilienceFactors;

	// Projected Outcomes: 장기 예측 결과
	public String projectedOutcomes;

	// Nested class to represent each historical pattern
	public static class HistoricalPatternDetail {
		// Event Date: 과거 사건의 날짜
		public LocalDate eventDate;

		// Pattern Description: 과거 사건의 특성과 현재와의 유사성
		public String patternDescription;

		// Market Impact: 주가에 미친 영향
		public String marketImpact;

		// Investor Action: 슈퍼개미의 통찰과 행동
		public String investorAction;
	}

	// Nested class to represent catalyst details
	public static class CatalystDetail {

		// Catalyst Description: 촉매 설명
		public String catalystDescription;

		// Expected Impact: 예상되는 장기 영향
		public String expectedImpact;

		// Historical Comparison: 과거의 유사 촉매와 비교
		public String historicalComparison;

		// Investor Insight: 슈퍼개미의 통찰력
		public String investorInsight;
	}
}
