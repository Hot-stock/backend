package com.bjcareer.GPTService.out.api.gpt.thema.stockNews;

import com.bjcareer.GPTService.out.api.gpt.PropertyArrayDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.bjcareer.GPTService.out.api.gpt.PropertyObject;
import com.bjcareer.GPTService.out.api.gpt.common.prompt.PositivePrompt;
import com.bjcareer.GPTService.out.api.gpt.thema.ThemaVariable;
import com.bjcareer.GPTService.out.api.gpt.thema.prompt.ThemaPrompt;

public class StockNewsThemaProperties {
	public static final String[] required = {"isRelated", "relatedDetail", "thema", "isPositive"};

	public PropertyDetail isRelated = new PropertyDetail("boolean", RelatedStockRaiseFilterPrompt.PROMPT);
	public PropertyDetail relatedDetail = new PropertyDetail("string", "isRelated의 결과에 대한 이유를 설명하세요! 한글로 답변하세요");
	public PropertyArrayDetail thema = new PropertyArrayDetail(ThemaPrompt.PROMPT,
		new PropertyObject(new ThemaVariable(), ThemaVariable.required));
	public PropertyDetail isPositive = new PropertyDetail("boolean", PositivePrompt.PROMPT);
}


