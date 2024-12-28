package com.bjcareer.GPTService.out.api.gpt.thema;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.themaName.ThemaPrompt;

public class ThemaVariable {
	public static final String[] required = {"name", "stockNames", "reason"};

	// Name: Theme for the stock based on specific keywords
	public PropertyDetail name = new PropertyDetail("string", ThemaPrompt.PROMPT);
	public PropertyArrayDetail stockNames = new PropertyArrayDetail(
		"name필드와 관련된 주식 종목들의 이름을 나열하세요. 반드시 기사에서 언급된 사실만을 사용하세요",
		new PropertyDetail("string", "name필드와 관련된 주식 종목들의 이름을 나열하세요. 반드시 기사에서 언급된 사실만을 사용하세요"));

	public PropertyDetail reason = new PropertyDetail("string",
		"1차 원인은 무엇인지에 대해서 설명해줘");

}
