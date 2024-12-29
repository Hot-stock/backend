package com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.NextSchedulePrompt;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonVariable;

public class ThemaProperties {
	public static final String[] required = {"isRealNews", "isRealNewsDetail", "summary", "upcomingDate",
		"upcomingDateReason", "stockNames"};

	public PropertyDetail isRealNews = new PropertyDetail("boolean", "Is this news an actual event?");
	public PropertyDetail isRealNewsDetail = new PropertyDetail("string",
		"Explain the results of the first stage guidelines and describe the specific conditions.");

	// Summary: Key summary and major events
	public PropertyDetail summary = new PropertyDetail("string",
		"Provide a summary of the theme's background mentioned in the questionnaire.");

	// Key Upcoming Dates: Major schedule and expected impact on the theme
	public PropertyDetail upcomingDate = new PropertyDetail("string", NextSchedulePrompt.PROMPT);
	public PropertyObject upcomingDateReason = new PropertyObject(new NextScheduleReasonVariable(), NextScheduleReasonVariable.required);
	public PropertyArrayDetail stockNames = new PropertyArrayDetail(
		"List the stock names related to the theme provided in the questionnaire. If none exist, return an empty object.",
		new PropertyDetail("string",
			"List the stock names related to the theme provided in the questionnaire. If none exist, return an empty object."));

}
