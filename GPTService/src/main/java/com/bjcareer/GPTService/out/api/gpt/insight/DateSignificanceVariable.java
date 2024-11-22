package com.bjcareer.GPTService.out.api.gpt.insight;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class DateSignificanceVariable {
	@JsonIgnore
	public static final String[] required = {"date", "historicalImpact", "predictedImpact", "confidenceScore"};

	// The key date being evaluated
	public PropertyDetail date = new PropertyDetail("string",
		"Specify the key date (in YYYY-MM-DD format) that is anticipated to impact the stock. For example, '2024-11-12' for a significant policy announcement."
	);

	// Historical Impact: Historical context for similar dates
	public PropertyDetail historicalImpact = new PropertyDetail("string",
		"Describe historical impacts of similar events on this date or around the same period. Include specific examples from past occurrences, "
			+ "such as 'On similar policy announcement dates in 2016 and 2019, this stock’s sector saw an average increase of 5-7% within two days.' "
			+ "This section should detail the stock or sector behavior following similar past events, referencing reliable sources or data where possible."
	);

	// Predicted Impact: Expected effect based on historical trends
	public PropertyDetail predictedImpact = new PropertyDetail("string",
		"Provide an anticipated impact for this date based on past trends. For example, 'Given previous reactions to similar policy announcements, "
			+ "a 4-6% price increase within a week is expected if the announcement aligns with market expectations.' Support this prediction with historical examples and logic."
	);

	// Confidence Score: Level of confidence in the predicted impact
	public PropertyDetail confidenceScore = new PropertyDetail("integer",
		"Assign a confidence score (0-100) based on the strength of historical patterns and event similarity. Higher scores reflect strong historical alignment "
			+ "and reliable data, such as '90 – Historical events on this date consistently led to positive outcomes in similar conditions.' Lower scores indicate less certainty."
	);
}
