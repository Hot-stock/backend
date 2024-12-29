package com.bjcareer.GPTService.out.api.gpt.insight.trigger;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;

public class TriggerProperties {
	public static final String[] required = {"background", "keywords", "nextUse", "nextUseReason"};
	public PropertyDetail background = new PropertyDetail("string", "The key background knowledge of themes acquired in <reason></reason> the field Based on the phrasing and details provided in the question");

	public PropertyDetail nextUse = new PropertyDetail("string", "Generalize the underlying causes of an event or background knowledge to explain how it can be applied in other situations. Based on the phrasing and details provided in the question");
	public PropertyDetail nextUseReason = new PropertyDetail("string", "nextUse 답변이 100점짜리 답변인 이유에 대해서 말해줘");

	public PropertyArrayDetail keywords = new PropertyArrayDetail(
		"To trace the background of an event effectively, focus on three key elements: the individuals involved, the political events that influenced the situation, and the specific incident name or product name associated with it.",
		new PropertyDetail("string", "To trace the background of an event, focus on three key factors: the people involved, relevant political events, and the specific incident or product name linked to the situation."));
	;
}
