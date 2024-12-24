package com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.prompt;

public class RelatedFilterPrompt {
	public static final String PROMPT =
		"Act as a stock investor evaluating news articles. Analyze the provided article to assess its relevance to the specified theme."
			+ " Based on the article, develop actionable insights focusing on theme stock opportunities."
			+ " Generate three ideas and evaluate their probability of success, usefulness, and the level of public attention they can attract."
			+ " Propose methods to enhance the public's understanding of the news through three distinct platforms and approaches.\n\n"

			+ "For each idea, evaluate its relevance to the theme, potential profitability in stock investments, and its ability to garner attention."
			+ " Refine ideas for clarity and structure while analyzing strengths and weaknesses."
			+ " Provide detailed recommendations and actionable steps.\n\n"

			+ "질문: 제공된 평가방법을 통해서 질문지의 기사가 테마에 관련된 뉴스인지 평가해줘.\n\n"

			+ " ** 평가 방법 **\n"

			+ "**1단계: 홍보성 기사 평가**\n"
			+ "- 기사의 내용이 특정 기업의 성과나 제품에 대한 내용이면 '관련 없음'으로 분류합니다.\n\n"
			+ "- 다음 단계로 진행합니다.\n\n"

			+ "**2단계: 테마 정보성 평가**\n"
			+ "- 추론이나 유추를 사용하지 않고, 기사에서 언급된 사실만을 사용해 평가합니다.\n"
			+ "- 기사의 내용이 신업의 성장 전망에 대해서 직접적으로 다루고 있다면 '관련있음'으로 평가합니다. -> 산업군에 대해서 해당하는 내용임으로 \n"
			+ "- 기사의 내용이 기업에 대한 성장 전망을 다루고 있다면 '관련없음'으로 평가합니다. -> 단일 기업에만 해당되는 내용임으로 \n"
			+ "- 다음 단계로 진행합니다.\n\n"

			+ "**3단계: 기사의 중점성**\n"
			+ "- 기사의 내용이 테마에 대한 정보제공을 중점으로 다루고 있으면 '관련있음'으로 평가합니다.\n"
			+ "- 기사의 내용이 테마에 대한 정보제공 이외의 다른 산업에 대해서도 다루고 있다면 '관련없음'으로 평가합니다.\n"
			+ "- 다음 단계로 진행합니다.\n\n";
}
