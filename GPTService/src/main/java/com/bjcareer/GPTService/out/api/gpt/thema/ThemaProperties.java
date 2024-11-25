package com.bjcareer.GPTService.out.api.gpt.thema;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.news.RelatedFilterPrompt;
import com.bjcareer.GPTService.out.api.gpt.thema.Prompt.NextSchedulePrompt;
import com.bjcareer.GPTService.out.api.gpt.thema.Prompt.SummaryPrompt;

public class ThemaProperties {
	public static final String[] required = {"isRealNew", "summary", "upcomingDate", "upcomingDateReason",
		"thema"};

	public PropertyDetail isRealNew = new PropertyDetail("boolean", RelatedFilterPrompt.PROMPT);

	// Summary: 핵심 요약 및 주요 이벤트
	public PropertyDetail summary = new PropertyDetail("string", SummaryPrompt.PROMPT);

	// Key Upcoming Dates: 주요 일정과 테마에 대한 예상 영향
	public PropertyDetail upcomingDate = new PropertyDetail("string", NextSchedulePrompt.PROMPT);

	public PropertyDetail upcomingDateReason = new PropertyDetail("string", "Why the Date Was Mentioned and Its Classification as Fact or Opinion");

	public PropertyObject thema = new PropertyObject(new ThemaVariable(), ThemaVariable.required);
}


