package com.bjcareer.GPTService.out.api.gpt.news;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.NextSchedulePrompt;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonVariable;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.KeywordPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.NameFilterPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.NewsFilterPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.RoseReasonPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.ThemaFilter;

public class NewsProperties {
	public static final String[] required = {"isRelevant", "isRelevantDetail", "isThema", "keywords", "name", "reason", "next",
		"nextReason"};
	public PropertyDetail isRelevant = new PropertyDetail("boolean", NewsFilterPrompt.FILTER_PROMPT);
	public PropertyDetail isRelevantDetail = new PropertyDetail("string", "isRelevant의 필드에 대한 결과에 대한 이유를 한글로 안내해주세요");
	public PropertyDetail isThema = new PropertyDetail("boolean", ThemaFilter.PROMPT);
	public PropertyArrayDetail keywords = new PropertyArrayDetail("기사에서 중점으로 다루고 있는 주식 상승의 이유에 대한 키워드를 추출",
		new PropertyDetail("string", KeywordPrompt.PROMPT));
	public PropertyDetail name = new PropertyDetail("string", NameFilterPrompt.PROMPT);
	public PropertyDetail reason = new PropertyDetail("string", RoseReasonPrompt.PROMPT);
	public PropertyDetail next = new PropertyDetail("string", NextSchedulePrompt.PROMPT);
	public PropertyObject nextReason = new PropertyObject(new NextScheduleReasonVariable(),
		NextScheduleReasonVariable.required);

}
