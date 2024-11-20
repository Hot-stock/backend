package com.bjcareer.search.out.api.gpt.thema;

public class RelatedFilterPrompt {
	public static final String PROMPT =
		"Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will contribute one step of their thought process,\n"
			+ "iteratively refining the analysis through group discussion.\n"
			+ "If an error is identified in any reasoning, it will be corrected or excluded from the discussion.\n"
			+ "The question is as follows:\n\n"

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
			+ "- If classified as 'IRRELEVANT,' return FALSE.\n";
}
