package com.bjcareer.GPTService.out.api.gpt.insight.score;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class InsightProperties {

	@JsonIgnore
	public static final String[] required = {"isFound", "eventsDetail", "insight"};

	// Market Drivers: 주요 시장 요인 분석 및 과거 데이터와의 비교
	public PropertyDetail isFound = new PropertyDetail("boolean", "news 테마의 트리거 또는 주식 가격 상승 내역과 연결 됐는지 알려주세요.");
	public PropertyDetail eventsDetail = new PropertyDetail("string", "추출된 이벤트들의 이름을 알려주고, 왜 해당 이벤트 이름이 나왔는지 알려주세요");
	public PropertyObject insight = new PropertyObject(new InsightVariable(), InsightVariable.required);
}
