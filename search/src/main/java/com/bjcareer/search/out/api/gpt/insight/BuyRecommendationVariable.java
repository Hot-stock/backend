package com.bjcareer.search.out.api.gpt.insight;

import com.bjcareer.search.out.api.gpt.PropertyDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BuyRecommendationVariable {

	@JsonIgnore
	public static final String[] required = {"score", "reason"};

	// Buy Recommendation Score, using a Time-Travel Investment Style
	public PropertyDetail score = new PropertyDetail("integer",
		"Provide a buy recommendation score from 0 to 100, influenced by the strength of catalysts with a historical basis. This score should reflect "
			+ "confidence in the stock’s potential, based on recurring historical events and patterns. Categorize the score as follows:\n"
			+ "- **Strong Buy (90-100)**: Historical patterns and catalysts strongly indicate upward potential, showing similar past events led to gains with low risk.\n"
			+ "- **Buy (70-89)**: Positive outlook based on historical catalysts, though with moderate risks. Historical events suggest gains but with occasional volatility.\n"
			+ "- **Hold (50-69)**: Neutral outlook, where historical trends show mixed results, warranting caution due to potential risk factors.\n"
			+ "- **Sell (30-49)**: Past patterns suggest downside risks outweigh potential gains, as previous similar conditions led to declines.\n"
			+ "- **Strong Sell (0-29)**: High risk with clear indicators of potential decline. Historical data reveals weak catalysts and low performance in similar situations."
	);

	// Rationale for Buy Recommendation, emphasizing historical catalysts and recurring patterns
	public PropertyDetail reason = new PropertyDetail("string",
		"Provide a detailed rationale for the score, focusing on how past patterns, historical catalysts, and recurring events support this recommendation. "
			+ "Use examples of specific historical events (e.g., past policy shifts, sector trends, or earnings releases) that align with the current situation, explaining "
			+ "how these patterns might influence stock movement. For example, 'In previous economic expansions, this sector gained steadily following policy announcements, "
			+ "suggesting a high likelihood of similar performance now.' Include factors influencing confidence or caution."
	);
}
