package com.bjcareer.search.out.api.gpt.news;

public class NewsFilterPrompt {
	public static final String FILTER_PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down 1 step of their thinking,\n"
			+ "then share it with the group.\n"
			+ "Then all experts will go on to the next step, etc.\n"
			+ "If any expert realises they're wrong at any point then they leave.\n"

			+ "THE QUESTION IS I want to determine if the article is fake news.\n"

			+ "Step 1: **Evaluate Relevance of Stock and Explanation**\n"
			+ "- Check if the `reason` field and `name` field are related to the stock mentioned in the article.\n"
			+ "- If neither the `reason` field nor the `name` field provides any relevant connection to the stock, classify as FAKE NEWS and terminate further processing.\n"
			+ "- If the fields indicate relevance to the stock, proceed to Step 2.\n\n"

			+ "Step 2: **Evaluate Rationality of Stock Movement Explanation**\n"
			+ "- Use the `reason` field to assess if the summarized content provides a rational explanation for the stock’s increase.\n"
			+ "- Check if the `reason` field includes clear and specific causes, such as significant company announcements, product launches, or policy changes directly impacting the stock.\n"
			+ "- Indirect factors, such as 'large purchases,' 'psychological factors,' 'chart patterns,' 'volume increase,' 'investor interest,' or 'general market interest,' lack clarity on cause and will be classified as FAKE NEWS.\n"
			+ "- If the `reason` field provides a clear, rational explanation, proceed to Step 3.\n\n"

			+ "Step 3: **Check for Specific Event-Based Causes**\n"
			+ "- If a significant, specific event is cited as the cause of the stock increase (e.g., a conflict, political statement, or major news involving the stock itself), mark it as REAL NEWS.\n"
			+ "- However, if the news centers around events like reaching a 'price limit' or reports of 'foreign investors bought shares,' or 'IPO' classify as FAKE NEWS, as they do not explain the stock movement’s root cause.\n"
			+ "- If the news is not event-based, proceed to Step 4.\n"

			+ "Step 4: **Financial Grounds Check**\n"
			+ "- Assess if the stock increase is attributed to broad financial indicators such as 'strong financials' or 'performance of similar sector stocks.'\n"
			+ "- If this is the only explanation, it will be classified as FAKE NEWS; otherwise, as REAL NEWS.\n\n"

			+ "**Final Decision**\n"
			+ "- If determined as FAKE NEWS, return TRUE.\n"
			+ "- If determined as REAL NEWS, return FALSE.";
}
