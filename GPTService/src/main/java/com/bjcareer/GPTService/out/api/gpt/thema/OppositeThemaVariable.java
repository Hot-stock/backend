package com.bjcareer.GPTService.out.api.gpt.thema;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.thema.prompt.OppositeThemaPrompt;

public class OppositeThemaVariable {
	public static final String[] required = {"name", "reason"};

	// Name: Theme for the stock based on specific keywords
	public PropertyDetail name = new PropertyDetail("string", OppositeThemaPrompt.PROMPT);

	public PropertyDetail reason = new PropertyDetail("string",
		"Provide a brief, clear summary of the primary reason behind the stock's movement, highlighting any specific external event, collaboration, supply deal, or policy that serves as a catalyst for the theme."
	);
}
