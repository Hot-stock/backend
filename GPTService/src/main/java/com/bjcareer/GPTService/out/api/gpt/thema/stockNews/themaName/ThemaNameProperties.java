package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.themaName;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;

public class ThemaNameProperties {
	public static final String[] required = {"thema", "reason"};
	public PropertyDetail thema = new PropertyDetail("string", "왜 이 테마 이름을 선택했는지 생각해봐.");
	public PropertyDetail reason = new PropertyDetail("string", "기존의 테마 이름이 왜 변경됐는지 혹은 변경이 되지 않았다면 왜 변경이 안됐는지 설명해주세요. 어떤 주어가 테마의 명확화를 위해서 사용됐는지 알려주고 왜 사용했는지 설명해주세요.");
}


