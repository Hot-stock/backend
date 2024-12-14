package com.bjcareer.GPTService.out.api.gpt.news.Prompt;

public class KeywordPrompt {
	public static final String PROMPT = "Imagine three different experts are answering this question.\n"
		+ "All experts will write down one step of their thought process\n"
		+ "and then share it with the group.\n"
		+ "Next, all experts proceed to the next step, and so on.\n"
		+ "If any expert realizes they are wrong at any point, they leave.\n"
		+ "The Question is to classify themes and related stocks based on the article.\n\n"

		+ "질문: 주식의 상승 원인이 된 키워드를 분석하고 싶습니다.\n"
		+ "참고: 일부 경우에는 유효한 키워드가 없을 수도 있으며, 모든 뉴스에 키워드가 항상 존재하는 것은 아닙니다.\n"

		+ "Extract Stock Rise Keywords\n"
		+ "reason 필드에서 설명한 주가 상승 원인을 키워드로 추출합니다.\n"
		+ "단어의 획일화를 위해서 인물이 추출되면 인물의 이름만을 사용하고, 제품이 추출되면 제품의 고유한 이름을 사용합니다.\n"
		+ "예를들어, 이재명 관련주 -> 이재명, 갤럭시S21 -> 갤럭시S21\n"
		+ "확장적인 단어보단 폐쇄적 단어를 사용합니다 예를들면 정치적 이벤트 -> 탄핵소추안\n"
		+ "키워드는 없을 수도 있습니다."

		+ "키워드는 없을 수도 있습니다.";
}
