package com.bjcareer.search.out.api.gpt.news;

import com.bjcareer.search.out.api.gpt.PropertyArrayObject;
import com.bjcareer.search.out.api.gpt.PropertyDetail;
import com.bjcareer.search.out.api.gpt.PropertyObject;

public class NewsProperties {
	public static final String[] required = {"filtered", "name", "reason", "themas", "next", "next_reason"};

	// IsFiltered: Determines if the rise is due to vague intrinsic factors vs. clear external events
	public PropertyDetail filtered = new PropertyDetail("boolean",
		"Set to true if the stock’s rise is attributed primarily to vague intrinsic factors, such as 'undervalued stock,' 'high investor interest,' 'profitability indicators,' or 'ROE.' " +
			"Only set to false if the rise is due to a specific, concrete external event directly impacting the stock, such as a 'new contract,' 'government policy announcement,' or 'major partnership.' " +
			"False should be used exclusively when a verifiable, external event provides a direct cause for the stock's movement. Avoid setting false if the reason centers around general market interest, " +
			"internal financial metrics, or broad terms like 'value stock' without concrete events."
	);


	// Name: Stock name
	public PropertyDetail name = new PropertyDetail("string",
		"Specify the stock name clearly in this field only. Do not use the stock name in other fields such as 'thema'."
	);

	// Reason: Brief summary of the reason for the stock's rise
	public PropertyDetail reason = new PropertyDetail("string",
		"Provide a concise summary (up to 3 lines) of the reason for the stock's rise. Highlight any connection to events, news, or prominent individuals."
	);

	// Thema: Stock’s theme based on reason keywords
	public PropertyArrayObject themas = new PropertyArrayObject(
		"Construct a theme for the stock using keywords derived from the 'reason' field. Prioritize significant entities (e.g., 'Trump', 'inflation') or key issues. Use one-word keywords separated by commas, with a maximum of 2 keywords. Avoid using the stock name itself as a theme."
		, new PropertyObject(new ThemaVariable(), ThemaVariable.required));

	// Next: Closest significant date relevant to the theme
	public PropertyDetail next = new PropertyDetail("string",
		"Identify the nearest significant date relevant to the theme, formatted as YYYY-MM-DD, based on today’s date. For approximate dates (e.g., 'next week'), use an inferred date (e.g., the closest Monday or Friday). Select dates with strong relevance, such as a policy announcement or industry event closely related to the theme, with at least 80% confidence in its relevance. If no reliable date can be inferred, leave this field empty."
	);


	// Next Reason: Explanation for choosing the date in "next"
	public PropertyDetail next_reason = new PropertyDetail("string",
		"Provide an explanation for the chosen date in 'next', describing the associated event and its relevance to the stock’s theme. If the timing is approximate (e.g., 'next week'), include this in the explanation. For example, if the date is inferred as a conference or industry update, explain its potential impact on the stock. If no strong reason exists, set this field to null."
	);

}
