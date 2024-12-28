package com.bjcareer.GPTService.out.api.gpt.insight.trigger;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;

public class TriggerProperties {
	public static final String[] required = {"background", "keywords", "nextUse", "nextUseReason"};
	public PropertyDetail background = new PropertyDetail("string", "<reason><reason>에서 습득한 테마의 주요 배경지식");

	public PropertyDetail nextUse = new PropertyDetail("string", "Generalize the underlying causes of an event or background knowledge to explain how it can be applied in other situations.");
	public PropertyDetail nextUseReason = new PropertyDetail("string", "nextUse 답변에 대한 이유를 여기에 적어줘");

	public PropertyArrayDetail keywords = new PropertyArrayDetail(
		"질문 3에 대한 답변을 여기에 해줘 키워드는 최대 3개만 추출해줘 인물순, 정치적 이벤트, 사건명이나 상품명",
		new PropertyDetail("string", "질문 3에 대한 답변을 여기에 해줘 키워드는 최대 3개만 추출해줘 인물순, 정치적 이벤트, 사건명이나 상품명"));
	;
}
