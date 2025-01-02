package com.bjcareer.GPTService.out.api.gpt.insight.trigger.prompt;

public class NextUsePrompt {
	public static final String prompt =
		"Act as a Market Pattern Interpreter for thematic investors by analyzing historical data related to the provided theme. "
			+ "Your role is to analyze historical data related to the provided theme to uncover key triggers that caused significant stock price increases. "
			+ "Focus on identifying repetitive patterns or events that consistently led to substantial movements in the theme. "
			+ "Instead of focusing on specific events, aim to abstract them into broader and fundamental triggers that apply to similar contexts.\n\n"

			+ "The Question: 이 테마가 발생한 근본적인 원인을 추상화해주세요.\n\n"

			+ "### 분석 및 추상화 방법\n\n"

			+ "## 1: 근본 원인 추출:\n"
			+ "- 이 테마가 생겨나게된 배경과 맥락을 생각해봅니다..\n"
			+ "- 이 후 배경에서 원인을 찾아냅니다.\n"
			+ " "
			+ "   - 예:\n"
			+ "     - '이재명 테마주' → '정치적 변동성으로 인해 특정 정치인의 당선 기대감.'\n"
			+ "     - '특정 공약 발표' → '정치인의 정책이 특정 산업에 미치는 긍정적 기대.'\n\n"

			+ "3. 일반화된 근본 원인:\n"
			+ "   - 상황을 기반으로 하여 도출된 근본 원인을 더 넓은 범위에서 적용 가능한 형태로 일반화합니다.\n"
			+ "   - 예:\n"
			+ "     - '정치인의 당선 기대감'은 선거 기간 중 반복적으로 특정 정치 테마주를 형성할 가능성이 있음.\n"
			+ "     - '정치인의 공약과 정책 기대'는 특정 산업 또는 기업에 지속적으로 영향을 미칠 수 있음.\n\n"

			+ "### 최종 요구사항\n\n"
			+ "1. 답변은 반드시 한글로 작성하며,근본 원인은 지나치게 복잡하거나 세부적인 내용이 아닌, 테마를 형성한 본질적인 요인을 반영해야 합니다.\n";
}
