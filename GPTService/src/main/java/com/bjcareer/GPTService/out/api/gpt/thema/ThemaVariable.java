package com.bjcareer.GPTService.out.api.gpt.thema;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.thema.Prompt.ThemaPrompt;

public class ThemaVariable {
	public static final String[] required = {"name", "reason"};

	// Name: Theme for the stock based on specific keywords
	public PropertyDetail name = new PropertyDetail("string", ThemaPrompt.PROMPT);

	public PropertyDetail reason = new PropertyDetail("string",
		"Provide a brief, clear summary of the primary reason behind the stock's movement, highlighting any specific external event, collaboration, supply deal, or policy that serves as a catalyst for the theme."
	);

}