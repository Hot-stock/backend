package com.bjcareer.GPTService.out.api.gpt.news;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.NextSchedulePrompt;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonVariable;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.NameFilterPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.NewsFilterPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.RoseReasonPrompt;

//한글보다 영어로가 더 좋다
//대문자로 쓰면 더 좋다.
//4o가 더 정확한 추론을 진행한다
public class NewsProperties {
	public static final String[] required = {"isRelevant", "name", "reason", "next", "nextReason"};
	public PropertyDetail isRelevant = new PropertyDetail("boolean", NewsFilterPrompt.FILTER_PROMPT);
	public PropertyDetail name = new PropertyDetail("string", NameFilterPrompt.PROMPT);
	public PropertyDetail reason = new PropertyDetail("string", RoseReasonPrompt.PROMPT);
	public PropertyDetail next = new PropertyDetail("string", NextSchedulePrompt.PROMPT);
	public PropertyObject nextReason = new PropertyObject(new NextScheduleReasonVariable(),
		NextScheduleReasonVariable.required);


}
