package com.bjcareer.GPTService.out.api.gpt.thema;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.thema.prompt.ThemaPrompt;

public class ThemaVariable {
	public static final String[] required = {"stockNames", "name", "reason"};

	// Name: Theme for the stock based on specific keywords
	public PropertyDetail name = new PropertyDetail("string", ThemaPrompt.PROMPT);
	public PropertyArrayDetail stockNames = new PropertyArrayDetail("추출된 테마가 상승 이유가 되는 종목들을 보여줘 반듯이 기사에 나온 사실만을 사용하고, 인과관계가 명확해야해",
		new PropertyDetail("string", "주식 이름"));

	public PropertyDetail reason = new PropertyDetail("string",
		"name필드의 값이 어떻게 나왔는지 설명해줘");

}
