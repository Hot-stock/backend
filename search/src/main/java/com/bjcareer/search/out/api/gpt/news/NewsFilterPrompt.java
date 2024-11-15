package com.bjcareer.search.out.api.gpt.news;

public class NewsFilterPrompt {
	public static final String FILTER_PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down 1 step of their thinking,\n"
			+ "then share it with the group.\n"
			+ "Then all experts will go on to the next step, etc.\n"
			+ "If any expert realizes they're wrong at any point, then they leave.\n"
			+ "The content of an article by a news reporter is provided within <article> </article> XML tags.\n"

			+ "\nStep 1: **Identify Relevance**\n"
			+ "- Examine if the content within <article> </article> mentions or is related to <stockName> </stockName>.\n"
			+ "- If the content is unrelated to <stockName>, classify it as FAKE NEWS and terminate further processing.\n"
			+ "- If relevant, proceed to Step 2.\n"

			+ "\nStep 2: **Summarize Content**\n"
			+ "- Summarize the article content within <article> </article>.\n"
			+ "- Remove XML tags in <article> </article>.\n"
			+ "- Continue to Step 3 with the summarized content.\n"

			+ "\nStep 3: **Evaluate Rationality of Stock Movement Explanation**\n"
			+ "- Identify if the summarized content provides a rational explanation for the stock’s increase.\n"
			+ "- Indirect factors, such as 'large purchases,' 'psychological factors,' 'chart patterns,' 'volume increase,' 'investor interest,' or 'general market interest,' lack clarity on cause and will be classified as FAKE NEWS.\n"

			+ "\nStep 4: **Check for Specific Event-Based Causes**\n"
			+ "- If a significant, specific event is cited as the cause of the stock increase (e.g., a conflict, political statement, or major news involving the stock itself), mark it as CLEAR NEWS.\n"
			+ "- However, if the news centers around events like reaching a price limit or reports of 'foreign investors bought shares,' classify as FAKE NEWS, as they do not explain the stock movement’s root cause.\n"

			+ "\nStep 5: **Evaluate Predictability of Future Events**\n"
			+ "- Any mention of unpredictable future events, such as 'bonus issues' or 'shareholding disclosures,' will be marked as FAKE NEWS due to the lack of a predictable schedule.\n"
			+ "- If no clear explanation is found, classify as FAKE NEWS; otherwise, proceed to Step 6.\n"

			+ "\nStep 6: **Event Keyword Comparison**\n"
			+ "- Analyze the content for key event-based phrases linked to significant stock movement events.\n"
			+ "- Common keywords include: 'merger', 'acquisition', 'lawsuit', 'CEO statement', 'political sanctions', 'trade ban', 'earnings report', 'product launch', 'contract signing', 'government approval'.\n"
			+ "- If any of these keywords are present and indicate a substantial reason for the stock’s change, classify as REAL NEWS.\n"
			+ "- If only vague or general terms appear without strong event linkage, classify as FAKE NEWS.\n"

			+ "\nStep 7: **Collaborative Verification of Event Authenticity (TOT)**\n"
			+ "- Experts will collaborate to verify if the event-based keywords detected in Step 6 actually indicate real, impactful news rather than FAKE NEWS.\n"
			+ "- Each expert will assess if the event is substantial enough or if it may be misleading by nature, such as exaggerated minor actions, speculative rumors, or events lacking verifiable sources.\n"
			+ "- Examples of misleading events might include vague insider rumors, unsupported predictions, or highly speculative statements about future stock performance.\n"
			+ "- **Collaborative Outcome**:\n"
			+ "    - If experts reach a consensus that the event is credible and significant, proceed as REAL NEWS.\n"
			+ "    - If they identify it as potentially misleading or unsubstantiated, classify it as FAKE NEWS.\n"

			+ "\n**Final Decision**\n"
			+ "- If determined as FAKE NEWS, return TRUE.\n"
			+ "- If determined as REAL NEWS, return FALSE.";
}
