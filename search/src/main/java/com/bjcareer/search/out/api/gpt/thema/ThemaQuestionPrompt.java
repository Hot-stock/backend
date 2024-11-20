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

			+ "### How to Determine Relevance to the Thema\n"

			+ "**Step 1: Evaluate Relevance Based on Summarized Content**\n"
			+ "- If the article only briefly mentions the thema without providing meaningful context, classify it as 'IRRELEVANT.'\n"
			+ "- Analyze the summarized content to determine if it is related to the thema.\n"
			+ "- If deemed relevant, proceed to the next step; otherwise, classify it as 'IRRELEVANT.'\n\n"

			+ "**Step 2: Assess the Purpose of the Article**\n"
			+ "- If the summarized content is primarily aimed at promoting a specific product or service rather than providing relevant information about the thema, classify it as 'IRRELEVANT.'\n"
			+ "- If the content only mentions the impact of the thema without addressing its causes or essential context, classify it as 'IRRELEVANT.'\n"
			+ "- If deemed relevant, proceed to the next step.\n\n"

			+ "**Step 3: Evaluate the Scope of Impact**\n"
			+ "- If the content is determined to have a nationwide impact, classify it as 'RELEVANT.'\n"
			+ "- If the scope of impact is limited to local economies, classify it as 'IRRELEVANT.'\n"
			+ "- If the thema's influence is restricted to a regional level, classify it as 'IRRELEVANT.'\n"
			+ "- Proceed to the final decision step.\n\n"

			+ "**Final Decision**\n"
			+ "- If all steps conclude the content as 'RELEVANT,' return TRUE.\n"
			+ "- If any step classifies the content as 'IRRELEVANT,' return FALSE.\n"

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
