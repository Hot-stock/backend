package com.bjcareer.GPTService.out.api.gpt.insight.score;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;

public class InsightVariable {
	public static final String[] required = {"insight", "insightDetail"};

	public PropertyDetail insight = new PropertyDetail("string",
		"추출된 이벤트들이 실행될 날짜와 이름과 내용들이 해당 주식 상승과 어떻게 연결되는지 논리적으로 설명하세요. 답변은 500자 이상으로 제한합니다. 답변은 한글로 작성해주세요.");
	public PropertyDetail insightDetail = new PropertyDetail("string",
		"도출된 인사이트들의 근가기 되는 기사들을 내용을 모두 작성해주세요 답변을 500자 이상으로 제한합니다");
}
