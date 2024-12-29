package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.themaName;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;

public class ThemaNameProperties {
	public static final String[] required = {"thema", "reason", "themasName"};
	public PropertyDetail thema = new PropertyDetail(
		"string",
		"가이드라인1을 적용한 결과를 알려주세요. Provide a 100-point answer that includes detailed reasoning, historical context, and connections to current trends or market demands. Your response should be thorough and demonstrate a comprehensive understanding of the theme's significance. 답변은 한국어로 고정합니다."
	);
	public PropertyDetail reason = new PropertyDetail(
		"string",
		"thema 필드가 나온 결과에 대한 이유를 알려주세요. Provide a 100-point answer that includes detailed reasoning, historical context, and connections to current trends or market demands. Your response should be thorough and demonstrate a comprehensive understanding of the theme's significance. 답변은 한국어로 고정합니다."
	);
	public PropertyDetail themasName = new PropertyDetail(
		"string",
		"최종 출력 결과에서 . Provide a 100-point answer that includes detailed reasoning, historical context, and connections to current trends or market demands. Your response should be thorough and demonstrate a comprehensive understanding of the theme's significance. 답변은 한국어로 고정합니다."
	);
}
