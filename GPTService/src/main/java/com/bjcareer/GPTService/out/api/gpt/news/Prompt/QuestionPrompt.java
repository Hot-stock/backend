package com.bjcareer.GPTService.out.api.gpt.news.Prompt;

import java.time.LocalDate;

import com.bjcareer.GPTService.config.AppConfig;

public class QuestionPrompt {
	public static String QUESTION_FORMAT =
		"이 뉴스의 발행일은 %s 입니다.\n"
			+ "오늘의 날짜는 " + LocalDate.now(AppConfig.ZONE_ID) + "입니다.\n"
			+ "주식 이름: <stock> %s </stock>\n"
			+ "기사 제목: %s\n"
			+ "기사 내용: <article> %s </article>\n" + "기사는 질문지에 제공한 주식 이름을 중점으로 분석해줘"

			+ "Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will contribute one step of their thought process,\n"
			+ "iteratively refining the analysis through group discussion.\n"
			+ "If an error is identified in any reasoning, it will be corrected or excluded from the discussion.\n"
			+ "The question is as follows:\n\n"

			+ "<question>"
			+ "1. Based on the content provided in the article, summarize why the stock specified within the <stock> tags has risen.\n"
			+ "2. Determine if the article is related to the stock.\n"
			+ "3. Classify the theme names and related stocks associated with the stock's rise based on the article.\n"
			+ "4. Determine whether the cause of the stock price increase applies only to the specific company mentioned in the article.\n"
			+ "5. Pinpoint the next event mentioned in the article and explain its significance.\n"
			+ "6. Extract Stock Rise Keywords.\n"
			+ "</question>\n\n"

			+ "Step 1: **Extract Stock Name and Remove XML Tags**\n"
			+ "- Retrieve the Stock name from within the <stock> </stock> XML tags.\n"
			+ "- Remove the XML tags within <article> </article>.\n\n"

			+ "Step 2: **Summarize the Content**\n"
			+ "- Summarize the content of the article provided within <article> </article>, focusing on the stock name.\n"
			+ "- Include details relevant to why the stock name might have risen.\n"
			+ "- Avoid speculative language and focus only on explicitly mentioned reasons.\n"
			+ "- Complete the summary and finalize the response.\n\n"

			+ "Step 3: **Check for Explicit Reasons**\n"
			+ "- Validate if the summarized reasons are explicitly supported by the article content.\n"
			+ "- Discard speculative or inferred reasons unless backed by clear evidence in the article.\n"
			+ "- Refine the summary to ensure it aligns strictly with the provided facts.\n\n"

			+ "Step 4: **Focus on Stock-Specific Reasons**\n"
			+ "- Ensure the summary answers why the stock name mentioned within <stock> </stock> might have risen.\n"
			+ "- Highlight only the reasons directly tied to the stock's performance as described in the article.\n"
			+ "- Eliminate any unrelated or tangential information to maintain focus."

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
			+ "- If any of the above criteria are met, classify the article as politically relevant and return 'true'.\n"

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
			+ "- If all steps conclude the content as 'RELEVANT,' return TRUE.\n"
			+ "- If any step classifies the content as 'IRRELEVANT,' return FALSE.\n"

			+ "Instructions:\n"
			+ "1. If the article mentions factors such as internal announcements, earnings reports, patents, mergers, or company-specific events, classify it as 'false' (single-company factor).\n"
			+ "   Example: 'Company A's earnings grew by 20%%, resulting in a stock price increase.' → false\n\n"

			+ "2. If the article highlights political, economic, or social events that affect multiple companies, or uses terms like 'related stocks,' classify it as 'true' (theme).\n"
			+ "   Example: 'Electric vehicle demand surges, boosting related stocks including Company A, B, and C.' → true\n\n"

			+ "3. Key decision criteria:\n"
			+ "   - Does the article mention multiple companies impacted by a common factor?\n"
			+ "   - Are external macroeconomic or industry-wide events driving the movement?\n"
			+ "   - Does the article explicitly reference 'related stocks' or thematic groups?\n\n"

			+ "Your output should be 'false' for single-company factors and 'true' for broader impacts.\n"
			+ "Process the following input:\n\n"
			+ "The question is: 'Does the article describe a stock price movement caused by factors specific to a single company, or is it related to a broader theme involving multiple companies?'\n"

			+ "### Explain the Importance of the Next Date\n"
			+ "**Step 1: Analyze the Importance of the Date**\n"
			+ "- Explain why the extracted date is important.\n"
			+ "- This step involves dividing the information into facts and opinions.\n\n"

			+ "**Fact**\n"
			+ "- Include only the information explicitly stated in the article about the date.\n"
			+ "- Example: If the date refers to a specific event (e.g., 'shareholder meeting,' 'new product launch,' 'policy implementation date'), explain what is explicitly stated.\n"
			+ "- Provide concrete data based on the article, quoting or paraphrasing directly from it.\n\n"

			+ "**Opinion**\n"
			+ "- Provide reasonable interpretations based on the content of the article.\n"
			+ "- Example: 'The stock price may rise on this date,' or 'This event is likely to attract investor attention.'\n"
			+ "- Add context or extrapolate logical conclusions beyond what is directly mentioned in the article.\n\n"

			+ "**Example**\n"
			+ "- Fact: 'According to the article, December 1, 2024, is the date when a major policy change will be implemented.'\n"
			+ "- Opinion: 'This policy change is expected to drive growth in the relevant industry and attract investor interest.'\n\n"

			+ "Extract Stock Rise Keywords\n"
			+ "reason 필드에서 설명한 주가 상승 원인을 키워드로 추출합니다.\n"
			+ "단어의 획일화를 위해서 인물이 추출되면 인물의 이름만을 사용하고, 제품이 추출되면 제품의 고유한 이름을 사용합니다."
			+ "확장적인 단어보단 폐쇄적 단어를 사용합니다 예를들면 정치적 이벤트 -> 탄핵소추안"
			+ "키워드는 없을 수도 있습니다."


			+ "### Output Requirements\n"
			+ "- The response must be written in Korean.\n";
}

