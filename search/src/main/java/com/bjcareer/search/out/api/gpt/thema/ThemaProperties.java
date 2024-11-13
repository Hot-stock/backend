package com.bjcareer.search.out.api.gpt.thema;

import com.bjcareer.search.out.api.gpt.PropertyArrayObject;
import com.bjcareer.search.out.api.gpt.PropertyDetail;
import com.bjcareer.search.out.api.gpt.PropertyObject;

public class ThemaProperties {
	public static final String[] required = {"related", "summary", "catalysts", "policyImpactAnalysis",
		"recoveryProjectDetails", "interestRateImpact", "marketOutlook", "scenarioAnalysis", "keyUpcomingDates",
		"investmentAttractiveness"};

	public PropertyDetail related = new PropertyDetail("boolean",
		"Step-by-Step Guidelines:\n" +
			"1. Identify the main topic of the news article based on the content provided.\n" +
			"2. CHECK IF THE ARTICLE DIRECTLY ADDRESSES THE TOPIC PROVIDED BY THE QUESTIONER.\n" +
			"   - If the article includes specific information, events, or announcements directly related to the questioner's topic, consider it relevant.\n" +
			"   - If the article only mentions the topic briefly or without meaningful context, consider it irrelevant.\n" +
			"3. IF THE NEWS ARTICLE IS RELEVANT TO THE TOPIC PROVIDED BY THE QUESTIONER, RESPOND WITH 'TRUE'.\n" +
			"4. IF THE NEWS DOES NOT COVERT the questioner's TOPIC, respond with 'FALSE'.\n" +
			"5. Ensure the response accurately reflects whether the article is relevant to the topic provided by the questioner."
	);


	// Summary: 핵심 요약 및 주요 이벤트
	public PropertyDetail summary = new PropertyDetail("string",
		"Provide a succinct summary of the theme’s current relevance. Highlight key drivers, past events, or significant market shifts influencing it. Detail any recurring patterns that reveal high-impact catalysts, helping to anticipate future moves in this theme."
	);

	// Catalysts: 주요 촉매와 예측된 영향
	public PropertyArrayObject catalysts = new PropertyArrayObject(
		"List the primary catalysts that historically or recently influenced the theme, such as policy changes, major investments, or critical industry announcements. Specify each catalyst’s direct impact on stock movement, including investor sentiment shifts or anticipated sector trends. Where possible, quantify expected movement (e.g., '% change').",
		new PropertyObject(new CatalystsVariable(), CatalystsVariable.required));

	// Policy Impact Analysis: 정책 변화의 영향과 예측
	public PropertyDetail policyImpactAnalysis = new PropertyDetail("string",
		"Analyze the influence of key policy changes, focusing on major elections or geopolitical shifts (e.g., new trade policies post-election). Detail how specific policies will impact core sectors within the theme, especially those with global trade, investment, or regulatory implications. Consider past instances of similar policy shifts and their outcomes on related stocks."
	);


	public PropertyDetail recoveryProjectDetails = new PropertyDetail("string",
		"Describe how the theme is tied to recovery or reconstruction efforts (e.g., Ukraine). List target companies or industries expected to benefit based on market demand or strategic involvement. Provide examples of similar past projects that led to long-term growth, discussing projected timelines and major investors."
	);

	// Interest Rate Impact: 금리 인상에 따른 영향
	public PropertyDetail interestRateImpact = new PropertyDetail("string",
		"Analyze the potential impact of rising interest rates, focusing on sectors vulnerable to credit conditions (e.g., real estate, construction). Explain how this could affect capital inflows, investment outlook, or risk sentiment, especially for companies within this theme. Reference similar historical periods to support predictions."
	);

	// Market Outlook: 주력 지역 내 시장 전망 및 안정성 평가
	public PropertyDetail marketOutlook = new PropertyDetail("string",
		"Project how the theme may evolve in major target regions (e.g., US, Middle East, Ukraine) based on economic and political trends. Discuss anticipated demand shifts or stability in investments, drawing on regional trends that have influenced market confidence or volatility in the past."
	);

	// Scenario Analysis: 정책 방향에 따른 시나리오 예측
	public PropertyDetail scenarioAnalysis = new PropertyDetail("string",
		"Create a scenario analysis based on different policy directions (e.g., aggressive vs. conservative trade policies). Predict how each scenario would affect profitability or strategic positioning for related companies. Reference similar scenarios to gauge probable impacts on stock performance within the theme."
	);

	// Key Upcoming Dates: 주요 일정과 테마에 대한 예상 영향
	public PropertyDetail keyUpcomingDates = new PropertyDetail("string",
		"List crucial dates expected to influence the theme over the next 6 to 12 months, such as policy announcements or international summits. Each date should be in YYYY-MM-DD format, with a brief explanation of its expected impact on this theme’s direction. Leave blank if no significant date is identified."
	);

	// Investment Attractiveness: 투자 매력도 평가
	public PropertyDetail investmentAttractiveness = new PropertyDetail("string",
		"Evaluate the theme’s investment appeal from a strategic viewpoint, analyzing short-term (next 3 months) and medium-term (6-12 months) factors. Detail elements likely to drive or inhibit investment, considering volatility, potential returns, and positioning within broader market trends."
	);
}
