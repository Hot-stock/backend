package com.bjcareer.search.out.api.gpt.insight;

import com.bjcareer.search.out.api.gpt.PropertyArrayObject;
import com.bjcareer.search.out.api.gpt.PropertyDetail;
import com.bjcareer.search.out.api.gpt.PropertyObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LongTermThesisVariable {

	@JsonIgnore
	public static final String[] required = {"historicalPatterns", "catalystAnalysis", "riskResilienceFactors",
		"projectedOutcomes"};

	// Historical Patterns: 장기적 패턴 및 유사한 과거 사건
	public PropertyArrayObject historicalPatterns = new PropertyArrayObject(
		"Identify and analyze historical patterns that mirror current conditions. Compare past economic cycles, industry trends, or key technological shifts to current market conditions. This allows a super-investor to recognize repeating patterns and apply them to long-term predictions.",
		new PropertyObject(new HistoricalPatternDetail(), HistoricalPatternDetail.required)
	);

	// Catalyst Analysis: 촉매 분석
	public PropertyArrayObject catalystAnalysis = new PropertyArrayObject(
		"Identify long-term catalysts and analyze their impact based on past occurrences. Highlight catalysts such as regulatory changes, technological breakthroughs, and economic shifts that could drive future growth, drawing direct comparisons with historical precedents.",
		new PropertyObject(new CatalystDetail(), CatalystDetail.required)
	);

	// Risk and Resilience Factors: 장기 리스크와 회복 가능성 분석
	public PropertyDetail riskResilienceFactors = new PropertyDetail("string",
		"Analyze anticipated long-term risks and the resilience factors of the stock. Compare with past events where similar risks were present, and identify how the market or company adjusted, adapted, or thrived in response."
	);

	// Projected Outcomes: 장기 예측 결과
	public PropertyDetail projectedOutcomes = new PropertyDetail("string",
		"Based on historical patterns and catalyst analysis, forecast long-term potential outcomes. Example: 'If the adoption curve for AI in healthcare follows a similar path to cloud adoption in the early 2000s, a sustained growth rate of 15-25% is expected over the next decade.'"
	);

	// Nested class to represent each historical pattern
	public static class HistoricalPatternDetail {
		@JsonIgnore
		public static final String[] required = {"eventDate", "patternDescription", "marketImpact", "investorAction"};

		// Event Date: 과거 사건의 날짜
		public PropertyDetail eventDate = new PropertyDetail("string",
			"Specify the date of the historical event, which shows parallels to current market conditions. Format: YYYY-MM-DD."
		);

		// Pattern Description: 과거 사건의 특성과 현재와의 유사성
		public PropertyDetail patternDescription = new PropertyDetail("string",
			"Describe the pattern or trend observed during this historical event and why it is relevant to current conditions. Example: 'In the 2008 financial crisis, companies with strong cash reserves outperformed as they were less impacted by liquidity constraints.'"
		);

		// Market Impact: 주가에 미친 영향
		public PropertyDetail marketImpact = new PropertyDetail("string",
			"Describe the long-term effect of this historical event on the market or sector. Example: 'From 2008 to 2012, companies in this sector showed a steady growth rate despite the broader economic downturn.'"
		);

		// Investor Action: 슈퍼개미의 통찰과 행동
		public PropertyDetail investorAction = new PropertyDetail("string",
			"Suggest the actions a super-investor might have taken during this period, informed by insights gained through pattern recognition. Example: 'In response to the 2008 market conditions, strategic accumulation of technology stocks with strong fundamentals was a successful approach.'"
		);
	}

	// Nested class to represent catalyst details
	public static class CatalystDetail {
		@JsonIgnore
		public static final String[] required = {"catalystDescription", "expectedImpact", "historicalComparison",
			"investorInsight"};

		// Catalyst Description: 촉매 설명
		public PropertyDetail catalystDescription = new PropertyDetail("string",
			"Describe the long-term catalyst, including potential regulatory changes, technology innovations, or shifts in market dynamics. Example: 'The implementation of AI-driven diagnostics in healthcare is expected to redefine the industry.'"
		);

		// Expected Impact: 예상되는 장기 영향
		public PropertyDetail expectedImpact = new PropertyDetail("string",
			"Analyze the potential long-term impact of this catalyst. Example: 'Healthcare AI is projected to grow at an annual rate of 20-30%, leading to substantial cost savings and market growth.'"
		);

		// Historical Comparison: 과거의 유사 촉매와 비교
		public PropertyDetail historicalComparison = new PropertyDetail("string",
			"Compare the current catalyst with a similar historical event. Example: 'AI in healthcare mirrors the early 2000s adoption of digital health records, which saw a rapid growth trajectory.'"
		);

		// Investor Insight: 슈퍼개미의 통찰력
		public PropertyDetail investorInsight = new PropertyDetail("string",
			"Provide an analysis on how a super-investor would interpret this catalyst, leveraging lessons from historical patterns. Example: 'Given the transformative potential of healthcare AI, a strategic position in foundational technology stocks could yield high returns.'"
		);
	}
}
