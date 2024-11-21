package com.bjcareer.search.out.api.gpt.news;

public class NewsFilterPrompt {
	public static final String FILTER_PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down 1 step of their thinking,\n"
			+ "then share it with the group.\n"
			+ "Then all experts will go on to the next step, etc.\n"
			+ "If any expert realises they're wrong at any point then they leave.\n"

			+ "THE QUESTION IS I want to determine if the article is FAKE NEWS.\n"

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
			+ "- If determined as REAL NEWS, return FALSE.";
}
