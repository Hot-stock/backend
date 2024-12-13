package com.bjcareer.GPTService.out.api.gpt.thema;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.thema.prompt.ThemaPrompt;

public class ThemaVariable {
	public static final String[] required = {"name", "stockNames", "reason"};

	// Name: Theme for the stock based on specific keywords
	public PropertyDetail name = new PropertyDetail("string", ThemaPrompt.PROMPT);
	public PropertyArrayDetail stockNames = new PropertyArrayDetail("추출된 테마가 상승 이유가 되는 종목들을 보여줘 반듯이 기사에 나온 사실만을 사용하고, 인과관계가 명확해야해",
		new PropertyDetail("string", "name필드와 관련된 주식 종목들의 이름을 나열하세요. 반드시 기사에서 언급된 사실만을 사용하세요"));

	public PropertyDetail reason = new PropertyDetail("string",
		"name 테마가 어떤 가이드라인에 의해서 생겼는지, 왜 생겨나야 했는지 설명하세요.");

}
