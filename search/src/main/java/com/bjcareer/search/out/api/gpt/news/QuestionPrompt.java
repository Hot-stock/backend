package com.bjcareer.search.out.api.gpt.news;

public class QuestionPrompt {
	public static String QUESTION_FORMAT =
		"Today's date (news publication date): %s\n" +
			"Stock name: <stockName>%s</stockName>\n" +
			"Article content: <article>%s</article>\n" +
			"PLEASE NOTE THAT THIS NEWS ARTICLE DOES NOT NECESSARILY EXPLAIN THE REASONS FOR STOCK PRICE INCREASES.\n\n"

			+ "Imagine a panel of three experts collaboratively addressing this question.\n" +
			"Each expert will document one step of their thought process\n" +
			"and then share it with the group for discussion.\n" +
			"The experts will proceed to the next step together, iteratively refining their analysis.\n" +
			"If any expert identifies an error in their reasoning, they will withdraw from the discussion.\n" +
			"The question is as follows:\n\n" +

			"<question>" +
			"1. Determine if the news is fake.\n" +
			"2. Summarize the reasons for the stock's price movement.\n" +
			"3. Identify potential themes the stock could belong to.\n" +
			"4. Pinpoint the next event mentioned in the article and explain its significance.\n" +
			"</question>\n\n"

			+ "### Steps to Summarize the Reason for Stock's Increase\n"
			+ "**Step 1: Extract Relevant Data from Article**\n"
			+ "- Remove XML tags within <article> </article>.\n"
			+ "- Extract the stock name within <stockName> </stockName>.\n\n"

			+ "**Step 2: Summarize the Content**\n"
			+ "- Summarize the article content, focusing on the stock name.\n"
			+ "- Include why the content is relevant and store the result in the `reason` variable.\n"
			+ "- If the stock names emphasized in this article differ from the stock names we are targeting, classify it as FAKE NEWS.\n"
			+ "- Proceed to the next step.\n\n"

			+ "### Steps to Identify Fake News\n"

			+ "**Step 1: Evaluate Relevance of Stock and Explanation**\n"
			+ "- Summarized content unrelated to the analyzed stock is classified as FAKE NEWS.\n"
			+ "- Proceed to Step 2.\n\n"

			+ "**Step 2: Evaluate Rationality of Stock Movement Explanation**\n"
			+ "- Check if the `reason` field includes clear and specific causes, such as significant company announcements, product launches, or policy changes directly impacting the stock.\n"
			+ "- Explanations relying solely on market participants' buying or selling behavior (e.g., 'individual investors' net buying') without tying to a significant event or business outcome are classified as FAKE NEWS.\n"
			+ "- If such causes are present, classify as FAKE NEWS. Otherwise, proceed to Step 3.\n\n"

			+ "**Step 3: Check for Specific Event-Based Causes**\n"
			+ "- Check if the `reason` field cites specific events such as 'price limit,' 'IPO,' or 'policy implementation.'\n"
			+ "- If the reason includes vague or secondary factors, such as 'net buying by individual investors' or 'net selling by institutions,' without any concrete event or corporate action, classify it as FAKE NEWS.\n"
			+ "- Indirect factors like 'chart patterns,' 'market momentum,' or 'sector interest' lacking clarity will be classified as FAKE NEWS.\n"
			+ "- If these events are cited, classify as FAKE NEWS; otherwise, if the event is specific and impactful, proceed to Step 4.\n\n"

			+ "**Step 4: Final Assessment**\n"
			+ "- Evaluate if the explanation is specific, direct, and clearly tied to the stock.\n"
			+ "- If the reasoning remains unclear or indirect, classify it as FAKE NEWS.\n"
			+ "- If the reasoning is clear and valid, classify it as REAL NEWS.\n\n"

			+ "**Final Decision**\n"
			+ "- If determined as FAKE NEWS, return TRUE.\n"
			+ "- If determined as REAL NEWS, return FALSE."
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
