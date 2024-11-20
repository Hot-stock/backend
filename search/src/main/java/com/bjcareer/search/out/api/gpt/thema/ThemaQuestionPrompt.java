package com.bjcareer.search.out.api.gpt.thema;

import java.time.LocalDate;

import com.bjcareer.search.config.AppConfig;

public class ThemaQuestionPrompt {
	public static final String QUESTION_PROMPT =
		"The publication date of this news is %s.\n"
			+ "Today's date is " + LocalDate.now(AppConfig.ZONE_ID) + "\n"
			+ "Thema name: <thema>%s</thema>\n"
			+ "Article content: <article>%s</article>\n"

			+ "Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will document one step of their thought process\n"
			+ "and then share it with the group for discussion.\n"
			+ "The experts will proceed to the next step together, iteratively refining their analysis.\n"
			+ "If any expert identifies an error in their reasoning, they will withdraw from the discussion.\n"
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

			+ "### How to Determine Relevance to the Thema\n" +

			"**Step 1: Identify the main topic of the news article**\n"
			+ "- Summarize the content and proceed to the next step.\n"

			+ "**Step 2: Identify the main topic of the news article**\n"
			+ "- Analyze the core content of the article to identify its main topic.\n"
			+ "- If the main topic is related to the thema but the article is promotional or focuses on advertising other products using the thema, classify it as 'IRRELEVANT.'\n"
			+ "- If the main topic overlaps with or is related to the questioner's topic, proceed to Step 2.\n\n"

			+ "**Step 2: Evaluate whether the article directly addresses the questioner's topic**\n"
			+ "- Determine if the article explicitly and clearly addresses the questioner's topic.\n"
			+ "  - Assess whether it includes specific events, policies, announcements, or other direct connections.\n"
			+ "  - If the content is overly general or lacks clear context, classify it as 'IRRELEVANT.'\n"
			+ "- If the article specifically addresses the questioner's topic, proceed to Step 3.\n\n"

			+ "**Step 3: Assess the market impact of the topic**\n"
			+ "- Evaluate whether the topic in the article has the potential to impact the stock market.\n"
			+ "- Analyze the potential effects on specific companies, industries, or market sentiment.\n"
			+ "- If the impact is minimal or ambiguous, classify it as 'IRRELEVANT.'\n"
			+ "- If the impact is significant, proceed to Step 4.\n\n"

			+ "**Step 4: Analyze the specificity and influence of the news**\n"
			+ "- Determine how closely the information in the article relates to the questioner's topic.\n"
			+ "- Check whether the article provides specific examples, data, or events.\n"
			+ "- If the information is vague or lacks specificity, classify it as 'IRRELEVANT.'\n"
			+ "- If the article is specific and clearly relevant to the questioner's topic, classify it as 'RELEVANT.'\n\n"

			+ "**Step 5: Handle multiple topics**\n"
			+ "- If the article covers multiple topics, evaluate whether the questioner's topic is central to the article.\n"
			+ "  - If the questioner's topic is not a key focus or is only indirectly related, classify it as 'IRRELEVANT.'\n"
			+ "  - If the questioner's topic is a major focus, classify it as 'RELEVANT.'\n"
			+ "- If the prominence of the topic is unclear, consider scoring the relevance of each topic to identify the most relevant articles.\n\n"

			+ "**Step 6: Evaluate the purpose of the news**\n"
			+ "- Determine whether the article primarily serves as a promotional piece for a specific product:\n"
			+ "  - Repeated mentions of product names or brand names.\n"
			+ "  - Frequent use of commercial language (e.g., 'bestseller,' 'popular choice').\n"
			+ "  - The main content focuses on product sales performance or consumer reactions, indicating a commercial intent.\n"
			+ "- Assess the **information-to-promotion ratio**:\n"
			+ "- If more than half of the content focuses on product promotion rather than providing new, independent information about the topic, classify it as 'IRRELEVANT.'\n"
			+ "- If the article primarily provides new or independent information on the questioner's topic, classify it as 'RELEVANT.'\n\n"

			+ "**Final Decision**\n"
			+ "- If classified as 'RELEVANT,' return TRUE.\n"
			+ "- If classified as 'IRRELEVANT,' return FALSE.\n"

			+ "### Steps to Find the Next Event\n"
			+ "**Step 1: Extract Closest Significant Future Date**\n"
			+ "- Provide the closest significant future date explicitly mentioned in the article related to the stock’s theme.\n"
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
