package com.bjcareer.search.out.api.gpt.news;

import com.bjcareer.search.out.api.gpt.PropertyDetail;

public class ThemaVariable {
	public static final String[] required = {"name", "reason"};

	// Name: Theme for the stock based on specific keywords
	public PropertyDetail name = new PropertyDetail("string",ThemaPrompt.THEMA_PROMPT);

	public PropertyDetail reason = new PropertyDetail("string",
		"Provide a brief, clear summary of the primary reason behind the stock's movement, highlighting any specific external event, collaboration, supply deal, or policy that serves as a catalyst for the theme."
	);


}
