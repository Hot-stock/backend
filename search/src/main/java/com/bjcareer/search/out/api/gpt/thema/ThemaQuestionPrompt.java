package com.bjcareer.search.out.api.gpt.thema;

import java.time.LocalDate;

import com.bjcareer.search.config.AppConfig;

public class ThemaQuestionPrompt {
	public static final String QUESTION_PROMPT =
		"이 뉴스의 발행일은 %s입니다.\n"
			+ "오늘의 날짜는 " + LocalDate.now(AppConfig.ZONE_ID) + "입니다.\n"
			+ "테마 이름: <thema>%s</thema>\n"
			+ "기사 내용: <article>%s</article>\n"

			+ "Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will contribute one step of their thought process,\n"
			+ "iteratively refining the analysis through group discussion.\n"
			+ "If an error is identified in any reasoning, it will be corrected or excluded from the discussion.\n"
			+ "The question is as follows:\n\n"

			+ "<question>"
			+ "1. Summarize the article.\n"
			+ "2. Determine if the article is related to the thema.\n"
			+ "3. Pinpoint the next event mentioned in the article and explain its significance.\n"
			+ "</question>\n\n"

			+ "### Steps to Summarize the Reason for Stock's Increase\n"
			+ "Step 1: **Extract Thema Name and Remove XML Tags**\n"
			+ "- Retrieve the thema name from within the <thema> </thema> XML tags.\n"
			+ "- Remove the XML tags within <article> </article>.\n"

			+ "Step 2: **Summarize the Content**\n"
			+ "- Summarize the content of the article provided within <article> </article>, focusing on the thema.\n"
			+ "- Complete the summary and finalize the response.\n"

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
			+ "- 단계를 통해 '관련 없음(IRRELEVANT)'으로 분류된 경우 FALSE를 반환합니다.\n"


			+ "### Steps to Find the Next Event\n"
			+ "**Step 1: Extract Closest Significant Future Date**\n"
			+ "- Provide the most important significant future date explicitly mentioned in the article related to the theme.\n"
			+ "- The extracted date must strictly be after the news publication date (today).\n"
			+ "- Date format: YYYY-MM-DD.\n"
			+ "- If no future date is mentioned or all mentioned dates are before or equal to today, leave this field empty.\n\n"

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

			+ "### Is this event recurring?\n"
			+ "**Step 1: Verify Recurrence of the Event**\n"
			+ "- Determine whether the event, such as a presidential election, drama season, or product launch, occurs periodically or is a one-time theme.\n"
			+ "- Structure the analysis by dividing it into facts and opinions for clarity.\n\n"

			+ "**Fact**\n"
			+ "- Base the response solely on information explicitly mentioned in the article.\n"
			+ "- Example: 'The drama’s second season will air later this month,' or 'The presidential election occurs every four years.'\n"
			+ "- Quote or paraphrase clear information from the article.\n\n"

			+ "**Opinion**\n"
			+ "- Provide an analysis or reasoning based on the facts.\n"
			+ "- Example: 'The airing of this drama season may positively impact related stocks,' or 'The presidential election is likely to draw market attention to policy-related themes.'\n"
			+ "- Extend logical inferences beyond the explicit content if supported by context.\n\n"

			+ "**Example**\n"
			+ "- Fact: 'According to the article, December 1, 2024, is the date of the presidential election.'\n"
			+ "- Opinion: 'The presidential election is likely to have a significant impact on policy-related stocks and may increase market volatility.'\n\n"

			+ "### 출력 요구사항\n"
			+ "- 응답은 반드시 한국어로 작성되어야 합니다.\n";
}
