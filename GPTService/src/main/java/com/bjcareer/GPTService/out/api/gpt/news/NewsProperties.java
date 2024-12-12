package com.bjcareer.GPTService.out.api.gpt.news;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.NextSchedulePrompt;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonVariable;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.NameFilterPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.NewsFilterPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.RoseReasonPrompt;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.ThemaFilter;

//한글보다 영어로가 더 좋다
//대문자로 쓰면 더 좋다.
//4o가 더 정확한 추론을 진행한다
public class NewsProperties {
	public static final String[] required = {"isRelevant", "isRelevantDetail", "isThema", "keywords", "name", "reason", "next",
		"nextReason"};
	public PropertyDetail isRelevant = new PropertyDetail("boolean", NewsFilterPrompt.FILTER_PROMPT);
	public PropertyDetail isRelevantDetail = new PropertyDetail("string", "isRelevant의 필드에 대한 결과에 대한 이유를 한글로 안내해주세요");
	public PropertyDetail isThema = new PropertyDetail("boolean", ThemaFilter.PROMPT);
	public PropertyArrayDetail keywords = new PropertyArrayDetail("주가 상승 원인의 키워드를 보여줘", new PropertyDetail("string",
		"주가 상승 원인의 키워드를 입력해줘"));
	public PropertyDetail name = new PropertyDetail("string", NameFilterPrompt.PROMPT);
	public PropertyDetail reason = new PropertyDetail("string", RoseReasonPrompt.PROMPT);
	public PropertyDetail next = new PropertyDetail("string", NextSchedulePrompt.PROMPT);
	public PropertyObject nextReason = new PropertyObject(new NextScheduleReasonVariable(),
		NextScheduleReasonVariable.required);

}
