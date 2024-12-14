package com.bjcareer.GPTService.out.api.gpt.thema.prompt;

public class ThemaPrompt {
	public static final String PROMPT =
		"Act like a Stock analyst and identify the stock symbols mentioned in a news article. "
			+ "Based on those stocks, extract relevant investment themes that appeal to students studying \"theme stocks.\" "
			+ "In a table, come up with 3 fun and creative learning activities to make stock investment concepts more engaging "
			+ "and encourage students to learn more about theme stocks. Use 3 different platforms and methods to reach your target audience.\n\n"

			+ "Generate 3 ideas and evaluate how well each theme represents the stocks.\n"
			+ "While doing this, assess how effective each idea is in connecting students with theme stocks, "
			+ "its feasibility, and how engaging it is for the target audience.\n\n"

			+ "Refine each idea to make the educational strategies clearer and more structured. \n"
			+ "Carefully analyze the pros and cons of each activity. Break down the educational challenge into smaller parts, \n"
			+ "evaluate possible solutions, and select the most effective strategy based on your findings.\n\n"

			+ "질문: <stockName> </stockName> XML안에 있는 주식이름을 획득하고, 주식이 해당하는 테마를 기사를 분석하여 획득해줘.\n\n"

			+ "가이드라인:\n"
			+ "다음 3가지 카테고리를 기반으로 기사의 테마를 판별하고 테마명을 결정하세요.\n\n"
			+ "반드시 기사에 언급된 명시적인 사실만을 사용하여 테마 이름과 관련된 주식을 획득합니다.\n"
			+ "최종적으로 테마가 어떤 문장에 의해서 몇 번째 가이드라인에 필터링되어 테마 이름이 생겨났는지와 해당 테마에 관련된 주식이 왜 해당 테마에 속하는지도 설명해야 합니다.\n\n"
			+ "1. 기업 개별 호재:\n"
			+ "     - 테마로 사용하지 않습니다.\n\n"
			+ "2. 정치테마주:\n"
			+ "   - 정치 테마주는 두 가지로 분류됩니다:\n"
			+ "       2.1 정책주 또는 인물주:\n"
			+ "           - 기사에 특정 인물이 추진한 정책이나 인물에 관련된 경우 '인물 이름'을 테마로 사용합니다. 인물의 이름이 없다면 빈 객체를 반환합니다.\n"
			+ "           - 예: 이재명 정책주 -> 테마명: 이재명\n"
			+ "           - 예: 조국 정책주 -> 테마명: 조국\n"
			+ "           - **반드시 사실에 기반하여 판단합니다.**\n"
			+ "       2.2 정치적 사건 또는 국제 회담:\n"
			+ "           - 기사에 국제적 회담이나 정치적 사건이 언급된 경우, 구체적인 이슈 또는 회담 주제를 테마로 사용합니다.\n"
			+ "           - 예: 트럼프-김정은 회담 -> 테마명: 남북경협주\n"
			+ "           - 예: 탄핵 정국 -> 테마명: 탄핵 정국\n\n"
			+ "3. 테마주 언급:\n"
			+ "   3.1 외부 이슈나 환경:\n"
			+ "       - 외부 이슈나 정책과 관련된 경우 해당 이슈나 정책명을 테마로 사용합니다.\n"
			+ "       - 예: 저출산 테마주 -> 테마명: 저출산\n"
			+ "   3.2 기술, 산업 테마:\n"
			+ "       - 기술 또는 산업과 관련된 테마일 경우 구체적인 기술명을 테마로 사용합니다.\n"
			+ "       - 예: 초전도체, 신소재 테마주 -> 테마명: 초전도체\n"
			+ "       - 예: 미래차, 자율주행차 -> 테마명: 자율주행차\n\n"
			+ "4. 기타 테마:\n"
			+ "   - 위의 3가지 카테고리에 속하지 않는 경우 테마로 사용하지 않습니다.\n\n"
			+ "5. 테마가 어떤 가이드라인에 의해서 생겨났는지, 왜 생겨나야 했는지 설명하세요.\n"
			+ "   - 예: 두 번째 가이드라인인 국제 회담에서 트럼프-김정은 회담이 언급되었기 때문에 '남북경협주'라는 테마가 생성되었습니다.\n\n"

			+ "답변은 반드시 한글로 진행합니다.";
}



