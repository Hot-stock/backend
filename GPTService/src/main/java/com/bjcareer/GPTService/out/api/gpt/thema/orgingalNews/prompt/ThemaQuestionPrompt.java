package com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.prompt;

import java.time.LocalDate;

import com.bjcareer.GPTService.config.AppConfig;

/**
 * This class provides a prompt for evaluating news articles based on their relevance to a specified theme.
 * The prompt guides experts through a step-by-step analysis process.
 */
public class ThemaQuestionPrompt {

	public static final String QUESTION_PROMPT =
		"이 뉴스의 발행일은 %s입니다.\n"
			+ "오늘의 날짜는 " + LocalDate.now(AppConfig.ZONE_ID) + "입니다.\n"
			+ "기사 내용: <article>%s</article>\n"
			+ "테마: <thema>%s</thema>\n\n"

			+ "Act as a stock investor evaluating news articles. Analyze the provided article to assess whether the content can provide insights related to the theme."
			+ "Based on the article, develop an insight strategy focusing on theme stock opportunities.\n"
			+ "Generate three actionable ideas and evaluate their probability of success, the usefulness of insights, and the level of public attention they can attract."
			+ "Additionally, propose methods to make the news more understandable and valuable to the public, utilizing three distinct platforms and approaches.\n\n"

			+ "For each generated idea, evaluate its relevance to the derived themes.\n"
			+ "Analyze the potential profitability of the idea in stock investments, the level of attention the information can garner, and its connection to theme stock investors.\n\n"

			+ "Refine each idea to make it clearer and more structured.\n"
			+ "Thoroughly analyze the strengths and weaknesses of each strategy, and break down the educational objectives into smaller, actionable steps.\n"
			+ "Consider various implementation scenarios, and recommend the most effective approach based on your analysis.\n\n"

			+ "질문: 제공된 평가방법을 통해서 질문지의 기사가 테마에 대한 정보를 제공하는 뉴스인지 평가해주세요.\n\n"

			+ " ** 평가 방법 **\n"

			+ "모든 평가에서는 사실만을 사용하고, 의견은 배제한다.\n"
			+ "각 단계에서 주어진 모든 조건에 대해서 '관련 있음'으로 분류되어야 받아야 다음 단계로 진행할 수 있습니다.\n"
			+ "각 단계에서 '관련 없음'으로 분류되면 즉시 false를 반환하고 평가를 종료합니다.\n"
			+ "각 조건에 대해서 왜 해당 평가를 받았는지 서술하세요.\n"
			+ "'**지역의 정책(도, 시, 군)의 정책이나 성과**'에 대한 내용을 다루고 있는 경우 '관련 없음'으로 분류합니다. 평가를 즉시 종료합니다.\n"

			+ "**1단계: 홍보성 기사 평가**\n"
			+ "1-1 '특정 기업'또는 '특정 지역'의 성과등만을 강조하고, 산업적 맥락 없이 소비 트렌드나 판매 데이터를 중심으로 작성된 기사는 '관련 없음'으로 분류합니다.\n"
			+ "1-2 '특정 주식의 상승 이유'를 다루고 있는 경우에는 '관련 없음'으로 분류됩니다.\n"
			+ "1-3 성공 사례를 중점으로 서술하고 있는 기사의 경우에도 '관련 없음'으로 분류됩니다.\n"
			+ "1-4 테마에 대한 문제점을 서술하고 해결책을 제시하는 기사는 '관련 없음'으로 분류됩니다.\n"
			+ "1-5 성과를 중점으로 다루고 있는 경우에는 '관련 없음'으로 분류됩니다.\n"
			+ "1-6 나머지의 경우 '관련 있음'으로 분류됩니다.\n"
			+ "1-7 지역에서 발생한 사건 사고의 경우에는 정치적으로 연관이 있어도 '관련 없음'으로 분류합니다.\n"
			+ "1-8 기사의 내용이 주가 변동에 관련되어 있다면 '관련 없음'으로 분류합니다.\n"
			+ "1-9 기사가 전반적인 시장 내용을 다루고 있다면 '관련 없음'으로 분류합니다.\n"
			+ "다음 단계를 진행합니다.\n\n"

			+ "**2단계: 기사 형식 평가 **\n"
			+ "- 기사의 형식이 사설, 칼럼, 수필 등의 형태로 사실 기반이 아니라 기자의 의견 기반으로 작성된 기사는 '관련 없음'으로 분류합니다.\n"
			+ "- 사건의 공론화를 위한 기사라면 '관련 없음'으로 분류합니다.\n"
			+ "- 정치적 인물이 직접적으로 연관된 사건으로 재판을 받는 경우가 아니라면 관련된 사건 사고 보도는 모두 '관련 없음'으로 분류합니다.\n"
			+ "- 기사가 단순한 축사나 테마에 대한 내용이 명시적으로 언급되지 않으면 '관련 없음'으로 분류합니다.\n"
			+ "- 기사가 '무엇을' '왜'등의 구체적인 맥락을 제공하지 못하고, '~~했다'등의 결과만 제공한다면 '관련 없음'으로 분류합니다.\n"
			+ "- '관련 없음'으로 뷴류되면 평가를 종료합니다.\n"
			+ " 다음 단계를 진행합니다.\n\n"

			+ "**3단계: 테마 정보성 평가**\n"
			+ "- 테마 이름이 인물이라면 \n"
			+ "-- 인물의 지지율 변화, 일신상의 변화, 정치적인 변화, 도전의 결심과 같은 내용은 '관련 있음'으로 평가됩니다.\n"
			+ "-- 인물의 성격, 가치관, 사회적 역할 등에 대한 내용은 '관련 없음'으로 평가됩니다.\n\n"

			+ "- 테마 이름이 기술이라면 \n"
			+ "-- 기술의 성능 향상, 기술 공개와 같은 내용이라면 '관련 있음'으로 평가합니다.\n"
			+ "-- 기술의 역사와 효과와 같은 내용이라면 '관련 없음'으로 평가합니다.\n\n"
			+ "-- 단순히 해당 기술을 사용하는 기업의 주식에 대한 변동성 내용이라면 '관련 없음'으로 평가합니다..\n\n"

			+ "- 그 외 테마는 아래의 가이드라인을 사용 \n"
			+ "- 기사에서 언급된 정책적 내용이 '특정 지역'의 정책이라면 '관련 없음'으로 분류됩니다.\n"
			+ "- 기사가 테마와 관련된 정책적 내용, 산업적 내용을 포함한다면 '관련 있음'으로 분류됩니다.\n"
			+ "- 기사가 테마의 내용과 비슷한 맥락에 대하여 위기 극복을 위한 의지를 피력하면 '관련 있음'으로 분류됩니다.\n"
			+ "- 특정 기업 또는 지역의 사례를 테마와 연결하려는 경우, 산업적 데이터나 맥락이 부족하면 '관련 없음'으로 평가합니다.\n"

			+ "- '관련 없음'으로 분류됐다면 다음 단계로 진행하지 않습니다.\n\n"

			+ "**4단계: 테마 중점성 평가**\n"
			+ "- 테마 이외의 주제나 다른 산업 동향, 소비 트렌드, 또는 특정 기업 성과에 중점을 둔 경우 '관련 없음'으로 평가합니다.\n"
			+ "- '관련 없음'으로 분류됐다면 다음 단계로 진행하지 않습니다.\n\n"

			+ "**4단계: 파급력 평가 **\n"
			+ "- 뉴스가 특정 발언인의 내용을 전달한다면 총수, 대통령, 정치인이 아닌 경우에는 파급력이 없음으로 '관련 없음'으로 분류합니다.\n"
			+ " 특정인의 발언이 테마를 해결해나고자 하는 의지나 도움을 구하는 내용이면 '관련 있음'으로 뷴류합니다.\n"

			+ "### 출력 요구사항\n"
			+ "- 관련있음으로 평가되면 isRealNews = true.\n"
			+ "- 그 외에는 isRealNews = false.\n"

			+ "### 출력 요구사항\n"
			+ "- 응답은 반드시 한국어로 작성되어야 합니다.\n"
			+ "- 각 단계별로 명확한 결론과 근거를 포함해야 합니다.\n"
			+ "- 최종 결과는 테마와 기사 간의 연관성 여부를 종합적으로 평가하여 판단합니다.";
}
