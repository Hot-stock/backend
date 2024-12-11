package com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.NextSchedulePrompt;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.PositivePrompt;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonVariable;
import com.bjcareer.GPTService.out.api.gpt.thema.prompt.RelatedFilterPrompt;
import com.bjcareer.GPTService.out.api.gpt.thema.prompt.SummaryPrompt;
import com.bjcareer.GPTService.out.api.gpt.thema.ThemaVariable;

public class ThemaProperties {
	public static final String[] required = {"isRealNew", "summary", "upcomingDate", "upcomingDateReason",
		"thema", "isPositive", "oppositeThema","isRealNewDetail"};

	public PropertyDetail isRealNew = new PropertyDetail("boolean", RelatedFilterPrompt.PROMPT);
	public PropertyDetail isRealNewDetail = new PropertyDetail("string", "isRealNew필드의 결과에 대한 이유를 설명하세요. 예를들면 몇 단계에서 필터링되서 결과값이 도출됐다. 답변은 한국어로 고정됩니다.");
	public PropertyDetail isPositive = new PropertyDetail("boolean", PositivePrompt.PROMPT);

	// Summary: 핵심 요약 및 주요 이벤트
	public PropertyDetail summary = new PropertyDetail("string", SummaryPrompt.PROMPT);

	// Key Upcoming Dates: 주요 일정과 테마에 대한 예상 영향
	public PropertyDetail upcomingDate = new PropertyDetail("string", NextSchedulePrompt.PROMPT);

	public PropertyObject upcomingDateReason = new PropertyObject(new NextScheduleReasonVariable(), NextScheduleReasonVariable.required);

	public PropertyObject thema = new PropertyObject(new ThemaVariable(), ThemaVariable.required);
	public PropertyObject oppositeThema = new PropertyObject(new OppositeThemaVariable(),
		OppositeThemaVariable.required);
}


