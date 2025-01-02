package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.prompt;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;

public class ThemaVariable {
	public static final String[] required = {"name", "stockNames", "reason"};

	// Name: Theme for the stock based on specific keywords
	public PropertyDetail name = new PropertyDetail("string", "가이드라인1을 적용한 결과를 알려주세요. 답변은 한국어로 고정합니다.");

	public PropertyArrayDetail stockNames = new PropertyArrayDetail(
		"List the names of stock items related to the 'name' field. Be sure to use only facts mentioned in the articles.",
		new PropertyDetail("string", "List the names of stock items related to the 'name' field. Be sure to use only facts mentioned in the articles.")
	);

	public PropertyDetail reason = new PropertyDetail("string",
		"name이 결과의 이유에 대해서 설명해주세요.!"
	);
}
