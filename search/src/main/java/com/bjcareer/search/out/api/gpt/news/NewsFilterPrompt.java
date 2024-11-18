package com.bjcareer.search.out.api.gpt.news;

public class NewsFilterPrompt {
	public static final String FILTER_PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down 1 step of their thinking,\n"
			+ "then share it with the group.\n"
			+ "Then all experts will go on to the next step, etc.\n"
			+ "If any expert realises they're wrong at any point then they leave.\n"

			+ "THE QUESTION IS I want to determine if the article is fake news.\n"

			+ "### Steps to Summarize the Reason for Stock's Increase\n"
			+ "**Step 1: Extract Relevant Data from Article**\n"
			+ "- Remove XML tags within <article> </article>.\n"
			+ "- Extract the stock name within <stockName> </stockName>.\n\n"

			+ "**Step 2: Summarize the Content**\n" +
			"- Summarize the article content, focusing on the stock name.\n" +
			"- Include why the content is relevant and store the result in the `reason` variable.\n" +
			"- If the stock names emphasized in this article differ from the stock names we are targeting, classify it as fake news. \n"
			+
			"- Proceed to the next step.\n\n" +

			"### Steps to Identify Fake News\n" +
			"**Step 1: Evaluate Relevance of Stock and Explanation**\n" +
			"- Summarized content unrelated to the analyzed stock is classified as FAKE NEWS.\n"
			+ "- Proceed to Step 2.\n\n" +

			"**Step 2: Evaluate Rationality of Stock Movement Explanation**\n" +
			"- Check if the `reason` field includes clear and specific causes, such as significant company announcements, product launches, or policy changes directly impacting the stock.\n"
			+ "- Indirect factors like 'foreign investor net purchases,' 'institutional or individual net buying,' or 'net purchases by any entity' are classified as FAKE NEWS due to lack of specificity.\n"
			+ "- If such causes are present, classify as FAKE NEWS. Otherwise proceed to Step 3.\n\n"

			+ "**Step 3: Check for Specific Event-Based Causes**\n"
			+ "- Check if the `reason` field cites specific events such as 'price limit,' 'foreign investors bought shares,' or 'IPO, will be classified as FAKE NEWS'"
			+ "  Check if it includes indirect factors like 'large purchases,' 'psychological factors,' 'chart patterns,' 'volume increase,' 'investor interest,' or 'general market interest,' which lack clarity on cause and will be classified as FAKE NEWS.\n"
			+ "- If these events are cited, classify as FAKE NEWS; otherwise, if the event is specific and impactful, proceed to Step 4.\n\n"

			+ "**Step 4: Financial Grounds Check**\n" +
			"- Evaluate if the stock increase is attributed to broad financial indicators such as 'strong financials' or 'performance of similar sector stocks.'\n"
			+ "- If specific supporting data is present, classify as REAL NEWS. Otherwise, classify as FAKE NEWS.\n\n"

			+ "**Final Decision**\n"
			+ "- If determined as FAKE NEWS, return TRUE.\n"
			+ "- If determined as REAL NEWS, return FALSE.";
}
