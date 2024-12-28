package com.bjcareer.GPTService.out.api.gpt.insight.trigger.prompt;

import java.time.LocalDate;

import com.bjcareer.GPTService.config.AppConfig;

public class QuestionPrompt {
	public static String QUESTION_FORMAT =
		"우리는 이 테마의 상승이유에 대한 트리거를 찾고 문장으로 다음 테마가 오를 때 선취매 할 수 있게 판단할려고 해. \n"
			+ "오늘의 날짜는 " + LocalDate.now(AppConfig.ZONE_ID) + "입니다.\n\n"
			+ "### 주식 정보\n"
			+ "- 테마 이름: <thema> %s </thema>\n"
			+ "- 상승 이유들: <reasons> %s </reasons>\n\n"

			+ "Act as a super-investor with the name '시간여행'. "
			+ "Analyze historical data related to the provided theme to uncover key triggers that caused significant stock price increases. "
			+ "Focus on repetitive patterns or events that consistently led to substantial movements in the theme. "
			+ "Based on these triggers, develop three actionable strategies for predicting and capitalizing on similar opportunities in the future. "
			+ "Evaluate each strategy in terms of its probability of success, impact on thematic investors, and potential to attract market attention. "
			+ "Propose methods for refining these strategies to enhance their clarity and accessibility, and break down each strategy into actionable steps. "
			+ "Finally, recommend the most effective approach to implement these strategies based on your analysis.\n\n"

			+ "<reasons> </reasons>에는 과거 상승 이유들이 요약되어 있으며, '\\n' 으로 구분되어 있습니다.\n"

			+ "질문 1: <reasons></reasons>에서 공통적인 패턴을 찾아서, 테마가 형성된 배경과 맥락 분석해주세요.\n"

			+ "### 방법\n\n"
			+ "### 1. 공통적 패턴 추출 방법\n"
			+ "1. <reasons> </reasons>에 과거 상승 이유들이 요약되어 있으며 ',' 으로 구분되어 있습니다.\n"
			+ "2. 이러한 이유들 중 맥락이 비슷한 내용을 클러스터링화합니다.\n"
			+ "3. 그 후 클러스터칭 내용을 생각해보고, 왜 이렇게 클러스터링 됐는지 설명합니다."

			+ "4. 클러스터링 내용중 특정 기업에만 해당되는 이유들은 제외합니다.\n"
			+ "ex) 특정 산업 진출 및 수주내용등은 특정 기업에만 해당되는 내용입니다.\n"

			+ "이유들을 분석하여 특정 사건이나 기업에 국한된 이유보다는, 테마 전반에 영향을 미친 공통적이고 근본적인 1차 원인을 식별해주세요.\n"
			+ "예를 들어:\n"
			+ "- '모듈러 건축 기업의 수익 증가'는 특정 기업 요인이지만, '우크라이나 전쟁으로 인한 재건 수요'는 테마를 설명하는 근본 원인입니다.\n\n"

			+ "### 2. 테마가 형성된 배경과 맥락 분석:\n"
			+ "   - 해당 테마가 왜 형성되었는지 정치적, 경제적, 사회적 요인을 분석하여 작성하세요.\n"
			+ "   - 예: '전쟁 발생 → 인프라 파괴 → 국제적 지원 필요 → 재건 테마 부상.'\n\n"

			+ "질문 2: 이 테마가 발생한 근본적인 원인을 추상화해주세요.\n\n"
			+ "## 분석 및 추상화 방법\n\n"

			+ "### 1. 테마의 배경 지식에서 발생 원인을 습득합니다.\n"
			+ "   - 테마의 이름이 발생하기 위한 근본적인 사건을 파악합니다.\n"
			+ "   - 예:\n"
			+ "    - '우크라이나 전쟁으로 인프라 파괴'\n"
			+ " 이를 1차 원인으로 지정합니다.\n\n"

			+ "### 2. 1차 원인 추상화 방법:\n"
			+ "   - 1차 원인을 통해 테마가 형성된 근본적인 원인을 추상화합니다.\n"
			+ "   - 예:\n"
			+ "     - '우크라이나 전쟁으로 인한 재건 테마' → '전쟁으로 인한 재건 테마'\n"

			+ "### 최종 요구사항\n\n"
			+ "1. 답변은 반드시 한글로 작성합니다";
}
