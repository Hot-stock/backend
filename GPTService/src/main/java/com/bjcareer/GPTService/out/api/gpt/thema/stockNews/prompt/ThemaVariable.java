package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.prompt;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;

public class ThemaVariable {
	public static final String[] required = {"name", "stockNames", "reason"};

	// Name: Theme for the stock based on specific keywords
	public PropertyDetail name = new PropertyDetail("string", ThemaPrompt.PROMPT);

	public PropertyArrayDetail stockNames = new PropertyArrayDetail(
		"List the names of stock items related to the 'name' field. Be sure to use only facts mentioned in the articles.",
		new PropertyDetail("string", "List the names of stock items related to the 'name' field. Be sure to use only facts mentioned in the articles.")
	);

	public PropertyDetail reason = new PropertyDetail("string",
		"Explain what the primary cause is."
	);
}
