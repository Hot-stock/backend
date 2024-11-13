package com.bjcareer.search.out.api.gpt.news;

import com.bjcareer.search.out.api.gpt.PropertyDetail;

public class ThemaVariable {
	public static final String[] required = {"name", "reason"};

	// Name: Theme for the stock based on specific keywords
	public PropertyDetail name = new PropertyDetail("string",
		"I will extract and catalog the theme representing the reason for the stock's increase.\n"
			+ "Step 1: First, refer to the reason field to gather the primary reason for the stock's increase.\n"
			+ "Step 2: Extract the name of the event that triggered the stock’s increase.\n"
			+ "   - Example 1: If Tesla's stock rose because of a new supercomputer, Tesla is the event, and the reason is the supercomputer.\n"
			+ "   - Example 2: If an individual’s action caused the event, such as Trump's statement leading to a stock increase, Trump becomes the event and the reason is the statement.\n"
			+ "   - Example 3: For themes like a push for resource production within a country, 'domestic' or 'resource' would be the event, and 'production push' the reason.\n"
			+ "Step 3: The most important rule is that the event name must **be specific and consist of only one word**.\n"
			+ "   - For instance, instead of 'trade dispute,' use 'US-China trade dispute' to ensure specificity.\n"
			+ "Think deeply and solve the issue.");

	public PropertyDetail reason = new PropertyDetail("string",
		"Provide a brief, clear summary of the primary reason behind the stock's movement, highlighting any specific external event, collaboration, supply deal, or policy that serves as a catalyst for the theme."
	);


}
