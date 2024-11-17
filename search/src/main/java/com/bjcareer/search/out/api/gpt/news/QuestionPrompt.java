package com.bjcareer.search.out.api.gpt.news;

public class QuestionPrompt {
	public static String QUESTION_FORMAT =
		"Today's date (news publication date): %s\n" +
			"Stock name: <stockName>%s</stockName>\n" +
			"Article content: <article>%s</article>\n" +

			"Imagine a panel of three experts collaboratively addressing this question.\n" +
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
			"</question>\n\n" +

			"### Steps to Summarize the Reason for Stock's Increase\n" +
			"**Step 1: Extract Relevant Data from Article**\n" +
			"- Remove XML tags within <article> </article>.\n" +
			"- Extract the stock name within <stockName> </stockName>.\n\n" +

			"**Step 2: Summarize the Content**\n" +
			"- Summarize the article content, focusing on the stock name.\n" +
			"- Include why the content is relevant and store the result in the `reason` variable.\n" +
			"- If the stock names emphasized in this article differ from the stock names we are targeting, classify it as fake news. \n" +
			"- Proceed to the next step.\n\n" +

			"### Steps to Identify Fake News\n" +
			"**Step 1: Evaluate Relevance of Stock and Explanation**\n" +
			"- If no rational explanation for the stock’s increase is present in the summarized content, classify as FAKE NEWS.\n"
			+
			"- Proceed to Step 2.\n\n" +

			"**Step 2: Evaluate Rationality of Stock Movement Explanation**\n" +
			"- Check if the `reason` field includes clear and specific causes, such as significant company announcements, product launches, or policy changes directly impacting the stock.\n"
			+
			"- If such causes are present, proceed to Step 3. Otherwise, classify as FAKE NEWS.\n\n" +

			"**Step 3: Check for Specific Event-Based Causes**\n" +
			"- Check if the `reason` field cites specific events such as 'price limit,' 'foreign investors bought shares,' or 'IPO.'\n"
			+
			"- If these events are cited, classify as FAKE NEWS.\n" +
			"- If the event is specific and impactful, proceed to Step 4.\n\n" +

			"**Step 4: Financial Grounds Check**\n" +
			"- Evaluate if the stock increase is attributed to broad financial indicators such as 'strong financials' or 'performance of similar sector stocks.'\n"
			+
			"- If specific supporting data is present, classify as REAL NEWS. Otherwise, classify as FAKE NEWS.\n\n" +

			"### Steps to Find the Stock's Theme\n" +
			"**Step 1: Identify Primary Reason**\n" +
			"- Refer to the 'reason' field to gather the primary reason for the stock's increase.\n" +
			"- Continue to Step 2 with the primary reason.\n\n" +

			"**Step 2: Extract Event Trigger Name**\n" +
			"- Extract the name of the event or entity that triggered the stock’s increase.\n" +
			"   - Example 1: If Tesla's stock rose due to a new supercomputer, 'Tesla' is the event, and 'supercomputer' is the reason. Companies mentioned in the question prompt can only be used as part of the reason, not the theme.\n" +
			"   - Example 2: If an individual's action caused the event, such as Trump's statement leading to a stock increase, 'Trump' becomes the event, and 'statement' is the reason.\n" +
			"   - Example 3: For broader themes, like a push for resource production within a country, 'domestic' would be the event, and 'production push' the reason.\n" +
			"- Proceed to Step 3 with the extracted event trigger name.\n\n" +

			"**Step 3: Ensure Event Specificity**\n" +
			"- The event name must be **specific and consist of only one word** to ensure clarity and focus.\n" +
			"- Use compound words or concise phrases where appropriate (e.g., '미중무역분쟁' for 'US-China trade dispute').\n" +
			"- Avoid using vague terms like 'trade issue' and prefer more precise terms like '미중무역분쟁.'\n\n" +

			"**Step 4: Exclude Stock-Intrinsic Events**\n" +
			"- If the event is intrinsic to the stock itself (e.g., 'IPO,' 'share buyback,' or 'price limit reached'), exclude it as a primary theme.\n" +
			"- Internal events are defined as actions or announcements initiated solely by the company and not influenced by external factors.\n" +
			"- Examples of excluded events: 'dividend announcement,' 'executive change,' or 'earnings report.'\n\n"

			+ "### Steps to Find the Next Event\n" +
			"**Step 1: Extract Closest Significant Future Date**\n" +
			"- Provide the closest significant future date explicitly mentioned in the article related to the stock’s theme.\n"
			+
			"- Date format: YYYY-MM-DD.\n" +
			"- If no future date is mentioned, leave this field empty.\n\n" +

			"**Step 2: Provide a Concise Explanation for the Date**\n" +
			"- Detail the relevance of the date to the theme.\n" +
			"- Include only direct information from the article without inference.\n" +
			"- If no date is provided, set this field to null.\n\n" +

			"### Output Requirements\n" +
			"- The response must be written in Korean.\n";
}
