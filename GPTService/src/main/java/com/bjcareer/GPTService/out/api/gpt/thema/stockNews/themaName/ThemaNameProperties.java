package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.themaName;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;

public class ThemaNameProperties {
	public static final String[] required = {"thema", "reason", "themasName"};
	public PropertyDetail thema = new PropertyDetail("string", "왜 이 테마 이름을 선택했는지 생각해봐.");
	public PropertyDetail reason = new PropertyDetail("string", "테마 이름이 왜 변경됐는지 혹은 변경이 되지 않았다면 왜 변경이 안됐는지 설명해주세요. 테마의 유사도 비교와 맥락을 비교하기 위해서 어떤 방법을 사용했는지 알려주세요");
	public PropertyDetail themasName = new PropertyDetail("string", "질문지에서 제공한 기존에 존제하는 테마 이름들을 모두 알려주세요");
}


