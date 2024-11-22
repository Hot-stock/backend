package com.bjcareer.GPTService.out.api.gpt.news;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayObject;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;

//한글보다 영어로가 더 좋다
//대문자로 쓰면 더 좋다.
//4o가 더 정확한 추론을 진행한다
public class NewsProperties {
	public static final String[] required = {"isFakeNews", "name", "reason", "themas", "next", "next_reason"};
	// Filtered: Determines if the article is fake news

	//for 4o
	public PropertyDetail isFakeNews = new PropertyDetail("boolean", NewsFilterPrompt.FILTER_PROMPT);

	// Name: Provides the exact stock name
	public PropertyDetail name = new PropertyDetail("string", NameFilterPrompt.PROMPT);

	// Reason: Brief summary for the stock's rise reason
	public PropertyDetail reason = new PropertyDetail("string", RoseReasonPrompt.PROMPT);

	// Theme: Identifies key themes related to stock increases.
	public PropertyArrayObject themas = new PropertyArrayObject(ThemaPrompt.THEMA_PROMPT,
		new PropertyObject(new ThemaVariable(), ThemaVariable.required)
	);
	public PropertyDetail next = new PropertyDetail(
		"string", NextSchedulePrompt.PROMPT);

	// Next Reason: Explanation for the selected date in "next"
	public PropertyDetail next_reason = new PropertyDetail(
		"string",
		"Why the Date Was Mentioned and Its Classification as Fact or Opinion."
	);

}
