package com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.NextSchedulePrompt;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonVariable;
import com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.prompt.SummaryPrompt;

public class ThemaProperties {
	public static final String[] required = {"isRealNews", "isRealNewsDetail", "summary", "upcomingDate",
		"upcomingDateReason", "stockNames"};

	public PropertyDetail isRealNews = new PropertyDetail("boolean", "이 뉴스가 실제 뉴스인가요?");
	public PropertyDetail isRealNewsDetail = new PropertyDetail("string",
		"1단계 가이드라인의 세부 조건들의 결과에 대해서 설명하고, 어떤 조건들이 있는지 알려주세요.");

	// Summary: 핵심 요약 및 주요 이벤트
	public PropertyDetail summary = new PropertyDetail("string", "기사를 바탕으로 주식 매매 아이디어를 도출해보고 도출할 수 없다면 빈 칸으로 둔다.");

	// Key Upcoming Dates: 주요 일정과 테마에 대한 예상 영향
	public PropertyDetail upcomingDate = new PropertyDetail("string", NextSchedulePrompt.PROMPT);
	public PropertyObject upcomingDateReason = new PropertyObject(new NextScheduleReasonVariable(), NextScheduleReasonVariable.required);
	public PropertyArrayDetail stockNames = new PropertyArrayDetail("질문지에서 제공한 테마와 관련된 주식 종목들을 나열해주세요. 항상 있는것이 아니며 없다면 빈 객체를 반환해주세요", new PropertyDetail("string", "질문지에서 제공한 테마와 관련된 주식 종목들을 나열해주세요. 항상 있는것이 아니며 없다면 빈 객체를 반환해주세요"));

}


