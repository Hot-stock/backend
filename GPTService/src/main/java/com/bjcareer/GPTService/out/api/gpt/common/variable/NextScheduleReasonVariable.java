package com.bjcareer.GPTService.out.api.gpt.common.variable;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.NextScheduleFactPrompt;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.NextScheduleOptinionPrompt;

public class NextScheduleReasonVariable {
	public static final String[] required = {"fact", "opinion"};

	public PropertyDetail fact = new PropertyDetail("string", NextScheduleFactPrompt.PROMPT);
	public PropertyDetail opinion = new PropertyDetail("string", NextScheduleOptinionPrompt.PROMPT);
}
