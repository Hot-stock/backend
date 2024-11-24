package com.bjcareer.GPTService.out.api.gpt.news;

import java.time.LocalDate;

import com.bjcareer.GPTService.config.AppConfig;

public class QuestionPrompt {
	public static String QUESTION_FORMAT =
		"이 뉴스의 발행일은 %s 입니다.\n"
			+ "오늘의 날짜는 " + LocalDate.now(AppConfig.ZONE_ID) + "입니다.\n"
			+ "주식 이름: <stock> %s </stock>\n"
			+ "기사 내용: <article> %s </article>\n"

			+ "Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will contribute one step of their thought process,\n"
			+ "iteratively refining the analysis through group discussion.\n"
			+ "If an error is identified in any reasoning, it will be corrected or excluded from the discussion.\n"
			+ "The question is as follows:\n\n"

			+ "<question>"
			+ "1. Summarize the article.\n"
			+ "2. Determine if the article is related to the stock.\n"
			+ "3. Pinpoint the next event mentioned in the article and explain its significance.\n"
			+ "</question>\n\n"

			+ "### Steps to Summarize article\n"
			+ "Step 1: **Extract Thema Name and Remove XML Tags**\n"
			+ "- Retrieve the thema name from within the <stock> </stock> XML tags.\n"
			+ "- Remove the XML tags within <article> </article>.\n"

			+ "Step 2: **Summarize the Content**\n"
			+ "- Summarize the content of the article provided within <article> </article>, focusing on the stock name.\n"
			+ "- Complete the summary and finalize the response.\n"

			+ "THE QUESTION IS: Determine if the article is related to a specific stock.\n\n"

			+ "### How to Determine Relevance to the Stock\n\n"

			+ "**Step 1: Evaluate Relevance Based on Summarized Content**\n"
			+ "- If the article only briefly mentions the theme without providing detailed or actionable context, classify it as 'IRRELEVANT.'\n"
			+ "- Summarize and analyze the content to determine its connection to the stock. Relevant content should contain concrete factors like events, announcements, or changes impacting the stock.\n"
			+ "- If deemed relevant, proceed to the next step; otherwise, classify it as 'IRRELEVANT.'\n\n"

			+ "**Step 2: Assess the Purpose of the Article**\n"
			+ "- Based on the summarized content, evaluate whether the article clearly explains the reason for the stock's increase.\n"
			+ "- Classify the article as 'IRRELEVANT' in the following cases:\n"
			+ "  1. If the explanation relies solely on financial metrics (e.g., past performance, generic financial analysis) that are not direct causes of the increase.\n"
			+ "  2. If it fails to provide clear external or internal reasons for the increase, such as significant investor activities, economic policies, or market events.\n"
			+ "- If none of the above applies, proceed to the next step.\n\n"

			+ "**Step 3: Analyze the Reasons for the Stock Increase**\n"
			+ "- Identify and explain the primary reasons for the stock's increase.\n"
			+ "- Classify the article as 'RELEVANT' if the increase is linked to:\n"
			+ "  1. Corporate growth factors, such as policy changes, new product launches, or mergers and acquisitions.\n"
			+ "  2. External events or conditions directly impacting the stock's performance, such as competitor disruptions, government subsidies, or major economic shifts.\n"
			+ "- If the reasons are unclear, speculative, or unrelated, classify it as 'IRRELEVANT.'\n\n"

			+ "**Final Decision**\n"
			+ "- If all steps conclude the content as 'RELEVANT,' return TRUE.\n"
			+ "- If any step classifies the content as 'IRRELEVANT,' return FALSE.\n"

			+ "### Steps to Find the Stock's Theme\n" +
			"**Step 1: Identify Primary Reason**\n" +
			"- Refer to the 'reason' field to gather the primary reason for the stock's increase.\n" +
			"- Continue to Step 2 with the primary reason.\n\n" +

			"**Step 2: Extract Event Trigger Name**\n" +
			"- Extract the name of the event or entity that triggered the stock’s increase.\n" +
			"   - Example 1: If Tesla's stock rose due to a new supercomputer, 'Tesla' is the event, and 'supercomputer' is the reason. Companies mentioned in the question prompt can only be used as part of the reason, not the theme.\n" +
			"   - Example 2: If an individual's action caused the event, such as Trump's statement leading to a stock increase, 'Trump' becomes the event, and 'statement' is the reason.\n" +
			"   - Example 3: For broader themes, like a push for resource production within a country, 'domestic' would be the event, and 'production push' the reason.\n" +
			"- Proceed to Step 3 with the extracted event trigger name.\n\n" +

			"**Step 3: Exclude Stock-Intrinsic Events**\n" +
			"- If the event is intrinsic to the stock itself (e.g., 'IPO,' 'share buyback,' or 'price limit reached'), exclude it as a primary theme.\n"
			+ "- Internal events are defined as actions or announcements initiated solely by the company and not influenced by external factors.\n"
			+ "- Examples of excluded events: 'dividend announcement,' 'executive change,' or 'earnings report.'"
			+ "- Proceed to Step 4 with the event trigger name.\n\n"

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

			+ "### Output Requirements\n"
			+ "- The response must be written in Korean.\n";
}
