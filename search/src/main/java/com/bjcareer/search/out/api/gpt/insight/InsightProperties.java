package com.bjcareer.search.out.api.gpt.insight;

import com.bjcareer.search.out.api.gpt.PropertyArrayObject;
import com.bjcareer.search.out.api.gpt.PropertyDetail;
import com.bjcareer.search.out.api.gpt.PropertyObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class InsightProperties {

	@JsonIgnore
	public static final String[] required = {"buyRecommendation", "marketDrivers",
		"volumeTargetForGain", "riskPeriods", "shortTermStrategy", "longTermThesis", "keyDates"};

	// Buy Recommendation: 매수, 보류, 매도 추천과 근거
	public PropertyObject buyRecommendation = new PropertyObject(new BuyRecommendationVariable(),
		BuyRecommendationVariable.required);

	// Market Drivers: 주요 시장 요인 분석 및 과거 데이터와의 비교
	public PropertyDetail marketDrivers = new PropertyDetail("string",
		"Identify the main forces affecting the stock, referencing similar past events with identical catalysts. Explain how industry trends, policy changes, or macroeconomic shifts have historically influenced similar stocks. Highlight the most impactful factor with data support."
	);

	// Key Dates: 주요 이벤트 날짜와 그 중요성
	public PropertyArrayObject keyDates = new PropertyArrayObject(
		"Identify all relevant dates (e.g., earnings, policy announcements) expected to impact stock performance. Provide a contextual summary of why each date matters, aligned with historical movement for similar events.",
		new PropertyObject(new KeyDateVariable(), KeyDateVariable.required)
	);

	public PropertyArrayObject shortTermStrategy = new PropertyArrayObject(
		"For each key date, upcoming dates, using historical data from similar events. For each date, list exact entry, monitoring, or exit actions. Reference past stock behavior, such as 'historically saw a 4-6% increase within 2 days.' Include probability of similar movement (e.g., '75% confidence based on past performance around similar events'). Each strategy corresponds to one date in keyDates.",
		new PropertyObject(new ShortTermStrategyVariable(), ShortTermStrategyVariable.required)
	);

	// Long Term Thesis: 6-12개월 투자 논리
	public PropertyArrayObject longTermThesis = new PropertyArrayObject(
		"Develop a 6-12 month thesis based on historical performance in similar conditions, with major growth catalysts and risks. Ensure each catalyst's historical accuracy is verifiable with at least one reliable source.",
		new PropertyObject(new LongTermThesisVariable(), LongTermThesisVariable.required)
	);

	// Volume Target for Gain: 예상 상승을 위한 거래량 분석
	public PropertyDetail volumeTargetForGain = new PropertyDetail("string",
		"Estimate the required trading volume for a 10% gain, using historical trading volumes in similar scenarios. Support with precise historical data from past catalysts that led to similar volume surges and their direct impact on price."
	);

	// Risk Periods: 하락 위험이 높은 기간 및 원인
	public PropertyDetail riskPeriods = new PropertyDetail("string",
		"Identify historical periods of volume drops and subsequent declines. Describe any recurring events that historically led to these risks (e.g., earnings disappointments), and state if current conditions mirror these past scenarios."
	);
}
