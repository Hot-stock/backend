package com.bjcareer.GPTService.out.api.gpt.thema.stockNews;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.PositivePrompt;
import com.bjcareer.GPTService.out.api.gpt.thema.ThemaVariable;
import com.bjcareer.GPTService.out.api.gpt.thema.prompt.ThemaPrompt;

public class StockNewsThemaProperties {
	public static final String[] required = {"thema", "isPositive"};
	public PropertyArrayDetail thema = new PropertyArrayDetail(ThemaPrompt.PROMPT,
		new PropertyObject(new ThemaVariable(), ThemaVariable.required));
	public PropertyDetail isPositive = new PropertyDetail("boolean", PositivePrompt.PROMPT);
}


