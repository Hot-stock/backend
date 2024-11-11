package com.bjcareer.search.out.api.gpt.thema;

import com.bjcareer.search.out.api.gpt.PropertyDetail;

public class CatalystsVariable {
	public static final String[] required = {"keyword", "catalyst"};

	// Keyword: Key descriptive term for the catalyst
	public PropertyDetail keyword = new PropertyDetail("string",
		"Specify a key term or phrase that encapsulates the catalyst theme (e.g., 'policy change', 'market expansion'). This keyword should summarize the main idea of the catalyst in one to two words."
	);

	// Catalyst: Primary catalyst details
	public PropertyDetail catalyst = new PropertyDetail("string",
		"Describe the primary catalyst influencing the theme, detailing historical or recent impacts. Include specific catalyst types (e.g., policy changes, major investments, or industry announcements) and explain how each has affected stock movement, investor sentiment, or sector trends. If possible, quantify the impact on stock movement (e.g., 'resulted in a 5% increase') and specify relevant events or examples."
	);
}
