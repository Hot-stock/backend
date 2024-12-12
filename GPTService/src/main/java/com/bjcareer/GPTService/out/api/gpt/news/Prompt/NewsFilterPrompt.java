package com.bjcareer.GPTService.out.api.gpt.news.Prompt;

public class NewsFilterPrompt {
	public static final String FILTER_PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "Each expert will write down one step of their reasoning,\n"
			+ "then share it with the group.\n"
			+ "Afterward, all experts will proceed to the next step together.\n"
			+ "If any expert realizes they are wrong at any point, they leave the discussion.\n\n"

			+ "THE QUESTION IS: Determine if the article is related to a specific stock.\n\n"

			+ "### How to Determine Relevance to the Stock\n\n"

			+ "단계 0: **주가 상승의 원인 식별**\\n"
			+ "- 기사 내 명시적으로 제시된 사실과 기업 활동의 직접적인 연결성을 평가합니다.\\n"
			+ "- 기사 내에서 <stock> </stock> 태그로 제공된 주식 이름에 해당하는 주가 상승 요인을 획득하지 못했다면 '관련없음'으로 분류합니다\\n"
			+ "- 지역적 또는 산업적 정책 변화가 구체적인 투자 계획 또는 활동이 포함되지 않은 경우, '관련 없음'으로 분류합니다.\\n"
			+ "- '민간 투자가 이어질 전망'과 같은 추측적 전망은 '관련 없음'으로 분류합니다.\\n"
			+ "- 특정 기업의 언급이 산업적 맥락에서 단순한 예시로 사용된 경우에도 '관련 없음'으로 분류합니다.\\n"
			+ "- 질문지의 주식이 특정 정치인에 연관된 테마주고, 해당 정치인의 변화에 대한 설명이 기사에 있다면 relevant and return 'true'.\n\n"


			+ "**Step 1: Initial Relevance Check**\n"
			+ "- If the article does not mention or is unrelated to the specific stock name provided in the question, classify it as 'IRRELEVANT.'\n"
			+ "- If the article only briefly mentions the stock name or theme without providing detailed or actionable context, classify it as 'IRRELEVANT.'\n"
			+ "- If the article mentions only the stock's performance (e.g., hitting upper limit or percentage increase) without explaining the underlying reasons, classify it as 'IRRELEVANT.'\n"
			+ "- If deemed relevant, proceed to the next step; otherwise, classify it as 'IRRELEVANT.'\n\n"

			+ "Step 2: Check for Political Relevance\n"
			+ "- Evaluate whether the article mentions political figures, events, or issues directly related to the stock.\n"
			+ "- Consider the following as indicators of political relevance:\n"
			+ "  1. Political figures: Specific mentions of politicians or election candidates (e.g., '안철수', '윤석열').\n"
			+ "- If any of the above criteria are met, classify the article as politically relevant and return 'true'.\n\n"

			+ "**Step 3: Assess the Purpose of the Article**\n"
			+ "- Based on the summarized content, evaluate whether the article clearly explains the reason for the stock's increase or performance.\n"
			+ "- Classify the article as 'IRRELEVANT' in the following cases:\n"
			+ "  1. If the explanation relies solely on financial metrics (e.g., past performance, generic financial analysis) that are not direct causes of the increase.\n"
			+ "  2. If it fails to provide clear external or internal reasons for the increase, such as significant investor activities, economic policies, or market events.\n"
			+ "  3. If the article mentions internal company strategies (e.g., restructuring, leadership changes) but lacks external validation or broader market implications, classify it as 'IRRELEVANT.'\n"
			+ "  4. If the article attributes the stock's increase to generic investor behavior (e.g., foreign or institutional buying), classify it as 'IRRELEVANT.'\n"
			+ "  5. If the article primarily discusses financial metrics (e.g., institutional net buying/selling, trading volume), classify it as 'IRRELEVANT.'\n"
			+ "  6. If the article attributes stock performance to projections or forecasts (e.g., positive outlook for revenue or profit) without specifying the source or context of the announcement, classify it as 'IRRELEVANT.'\n"
			+ "  7. If the article mentions only the stock's performance metrics (e.g., hitting upper limit or percentage increase) without providing the reasons behind the performance, classify it as 'IRRELEVANT.'\n\n"

			+ "- Additional Filtering:\n"
			+ "  - If the primary reason for the stock's rise is attributed to bargain hunting or general rebounds driven by foreign investor net buying, classify it as 'IRRELEVANT.'\n"
			+ "  - If the rise is deemed specific to the company and unlikely to affect other companies, classify it as 'IRRELEVANT.'\n"
			+ "  - Evaluate if the reasons provided have potential broader impacts on other companies. Use the following criteria:\n"
			+ "    1. The reasons should be based on broad external factors such as specific industries, policies, or economic changes.\n"
			+ "    2. Political events or policy announcements must clearly indicate potential impacts on the overall industry or market.\n"
			+ "    3. If political events are specified as affecting only a particular company, assess the scope explicitly.\n"
			+ "    4. Distinguish between facts and opinions: If the article relies on unsupported speculation or opinions, classify it as 'IRRELEVANT.'\n"
			+ "       - Example: Statements like 'This event might affect other countries' are speculative unless supported by data.\n"
			+ "    5. If the article highlights industry trends or policy ripple effects rather than individual company success, classify it as 'RELEVANT.'\n"
			+ "  - If the article does not clearly state the direct involvement of the specific stock in policies or events, classify it as 'IRRELEVANT.'\n"
			+ "  - If speculative phrases such as 'is expected to benefit' or 'may influence' are used without evidence, classify it as 'IRRELEVANT.'\n"
			+ "  - If the policies or events mentioned are generalized without clear focus on the specific stock, classify it as 'IRRELEVANT.'\n"
			+ "  - If the article primarily highlights awards, certifications, or internal programs that do not clearly link to financial or market performance, classify it as 'IRRELEVANT.'\n"
			+ "  - If the news improves brand image but lacks clear evidence of economic impact or market influence, classify it as 'IRRELEVANT.'\n\n"

			+ "**Step 4: Analyze the Reasons for the Stock Increase**\n"
			+ "- Identify and explain the primary reasons for the stock's increase or performance.\n"
			+ "- Classify the article as 'RELEVANT' if the increase is linked to:\n"
			+ "  1. Corporate growth factors, such as policy changes, new product launches, or mergers and acquisitions.\n"
			+ "  2. External events or conditions directly impacting the stock's performance, such as competitor disruptions, government subsidies, or major economic shifts.\n"
			+ "  3. Future events that are highly likely to impact the stock's performance, such as announced product launches, upcoming partnerships, or significant market expansions.\n"
			+ "- Classify the article as 'IRRELEVANT' if:\n"
			+ "  - The reasons are unclear, speculative, or unrelated to the stock's performance.\n"
			+ "  - The primary reasons do not represent actionable trends or impactful changes in the market or industry.\n"
			+ "  - The reasons rely only on investor behavior (e.g., net buying/selling) without additional external factors.\n"
			+ "  - The article only highlights stock performance metrics (e.g., hitting upper limit or percentage increase) without explaining the reasons behind the performance.\n\n"

			+ "**Final Decision**\n"
			+ "답변은 반듯이 한글로 해줘\n"
			+ "- If all steps conclude the content as 'RELEVANT,' return TRUE.\n"
			+ "- If any step classifies the content as 'IRRELEVANT,' return FALSE.\n";
}
