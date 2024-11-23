package com.bjcareer.GPTService.out.api.gpt.insight;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayObject;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

class ShortTermStrategyVariable {
	@JsonIgnore
	public static final String[] required = {"date", "catalystImpact", "historicalImpact", "predictedImpact",
		"probabilityOfIncrease", "investorInsight"};

	// Key Date: 전략의 핵심 날짜
	public PropertyDetail date = new PropertyDetail("string",
		"Specify the key date for this strategy, matching the dates in keyDates. Example: '2024-11-12'."
	);

	// Catalyst Impact: 촉매 영향 분석
	public PropertyDetail catalystImpact = new PropertyDetail("string",
		"Analyze how the specific catalyst on this date might impact stock performance, based on similar historical events. Specify why this catalyst is expected to influence the stock."
	);

	// Historical Impact: 유사한 과거 사건이 미친 영향
	public PropertyArrayObject historicalImpact = new PropertyArrayObject(
		"List historical events where similar catalysts led to stock price changes. Include date, reason for rise, and a reliable source link for verification.",
		new PropertyObject(new HistoricalImpactDetail(), HistoricalImpactDetail.required)
	);

	// Predicted Impact: 과거 경향에 따른 예상 영향
	public PropertyDetail predictedImpact = new PropertyDetail("string",
		"Predict the impact of this date's catalyst on stock performance based on past trends. Example: 'If the conditions align, a 4-6% increase is expected.'"
	);

	// Probability of Increase: 예상 상승 확률
	public PropertyDetail probabilityOfIncrease = new PropertyDetail("string",
		"Provide the estimated probability of a price increase, grounded in historical data. Example: 'There is an 85% chance of a 5% gain within 3 days following similar catalysts.'"
	);

	// Investor Insight: 슈퍼개미의 통찰력
	public PropertyDetail investorInsight = new PropertyDetail("string",
		"Provide an analysis on how a super-investor would interpret this catalyst, leveraging lessons from historical patterns. Example: 'Given the transformative potential of healthcare AI, a strategic position in foundational technology stocks could yield high returns.'"
	);

	public static class HistoricalImpactDetail {
		@JsonIgnore
		public static final String[] required = {"impactDate", "reasonForRise", "sourceLink"};

		// Impact Date: 과거 이벤트 발생 날짜
		public PropertyDetail impactDate = new PropertyDetail("string",
			"Specify the date of the past event. Format: YYYY-MM-DD. Example: '2022-05-15'."
		);

		// Reason for Rise: 주가 상승 이유
		public PropertyDetail reasonForRise = new PropertyDetail("string",
			"Explain the specific reason for the stock's rise on this date. Example: 'Exceeded earnings expectations.'"
		);

		// Source Link: 출처 링크
		public PropertyDetail sourceLink = new PropertyDetail("string",
			"Provide a source link verifying the event and reason for the stock's performance."
		);
	}
}
