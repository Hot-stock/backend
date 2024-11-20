package com.bjcareer.search.out.api.gpt.thema;

public class RelatedFilterPrompt {
	public static final String PROMPT =
		"Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will contribute one step of their thought process,\n"
			+ "iteratively refining the analysis through group discussion.\n"
			+ "If an error is identified in any reasoning, it will be corrected or excluded from the discussion.\n"
			+ "The question is as follows:\n\n"

			+ "### 테마와의 관련성을 판단하는 방법\n"

			+ "**단계 1: 요약된 내용을 바탕으로 테마와의 관련성 평가**\n"
			+ "- 기사에서 테마에 대한 간단한 언급만 있아면 '관련 없음(IRRELEVANT)'으로 분류합니다.\n"
			+ "- 요약된 내용을 분석하여 테마와 관련이 있는지 판단합니다.\n"
			+ "- 관련성이 있다고 판단되면 다음 단계로 진행하며, 아니라면 '관련 없음(IRRELEVANT)'으로 분류합니다.\n\n"

			+ "**단계 2: 기사의 정보 목적 평가**\n"
			+ "- 요약된 내용이 테마와 관련된 정보를 제공하기보다는 특정 제품이나 서비스의 홍보 목적으로 작성된 경우, '관련 없음(IRRELEVANT)'으로 분류합니다.\n"
			+ "- 테마와 관련된 영향만 언급되고, 테마의 발생 원인이나 본질적인 내용이 결여된 경우에도 '관련 없음(IRRELEVANT)'으로 분류합니다.\n"
			+ "- 관련성이 있다고 판단되면 다음 단계로 진행합니다.\n\n"

			+ "**단계 3: 영향력 평가**\n"
			+ "- 국가 단위에 영향을 미칠 수 있다고 판단하면 '관련 있음(RELEVANT)'으로 분류합니다\n"
			+ "- 기사에서 보여주는 영향력의 단위가 지역경제라고 판단되면 '관련 없음(RELEVANT)'으로 분류합니다.\n"
			+ "- 테마의 영향력이 지역적 수준에 머무른다고 판단되면 '관련 없음(IRRELEVANT)'으로 분류합니다.\n"
			+ "- 다음 단계를 진행합니다.\n\n"

			+ "**최종 결정**\n"
			+ "- 모든 단계를 거쳐 '관련 있음(RELEVANT)'으로 분류된 경우 TRUE를 반환합니다.\n"
			+ "- 단계를 통해 '관련 없음(IRRELEVANT)'으로 분류된 경우 FALSE를 반환합니다.\n";
}

// 테마 뉴스라는게 이 테마에 대한 구체적인 일정을 제공하거나, 정보를 제공해야 함
// 자율주행규제 완화소식에
