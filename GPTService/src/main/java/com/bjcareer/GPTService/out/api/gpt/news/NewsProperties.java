package com.bjcareer.GPTService.out.api.gpt.news;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.NextSchedulePrompt;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonVariable;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.KeywordPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.NameFilterPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.RoseReasonPrompt;

public class NewsProperties {
	public static final String[] required = {"isRelevant", "isRelevantDetail", "isThema", "keywords", "name", "reason", "next", "nextReason", "themaName", "themaReason", "themStockNames"};
	public PropertyDetail isRelevant = new PropertyDetail("boolean",
		"What is the result after applying GUIDELINE 1? A perfect 100-point answer is expected.");
	public PropertyDetail isRelevantDetail = new PropertyDetail("string",
		"A detailed explanation of the result isRelevant field. A perfect 100-point answer is expected. The answer should be in Korean.");

	public PropertyDetail isThema = new PropertyDetail("boolean",
		"What is the result after applying Guideline 2? A perfect 100-point answer is expected.");
	public PropertyArrayDetail keywords = new PropertyArrayDetail(
		"Extract the keywords related to the reasons for the stock increase emphasized in the article. A perfect 100-point answer is expected.",
		new PropertyDetail("string", KeywordPrompt.PROMPT));
	public PropertyDetail name = new PropertyDetail("string", NameFilterPrompt.PROMPT);
	public PropertyDetail reason = new PropertyDetail("string", RoseReasonPrompt.PROMPT);
	public PropertyDetail next = new PropertyDetail("string", NextSchedulePrompt.PROMPT);
	public PropertyObject nextReason = new PropertyObject(new NextScheduleReasonVariable(),
		NextScheduleReasonVariable.required);

	public PropertyDetail themaName = new PropertyDetail("string", "What is the name of the theme?");
	public PropertyDetail themaReason = new PropertyDetail("string", "What is the reason for the theme?");
	public PropertyArrayDetail themStockNames = new PropertyArrayDetail("What are the names of the stocks related to the theme?",
		new PropertyDetail("string", "Please enter the name of the stock related to the theme."));

}
