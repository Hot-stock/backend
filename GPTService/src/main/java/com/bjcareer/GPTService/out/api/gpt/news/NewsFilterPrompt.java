package com.bjcareer.GPTService.out.api.gpt.news;

public class NewsFilterPrompt {
	public static final String FILTER_PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "Each expert will write down one step of their reasoning,\n"
			+ "then share it with the group.\n"
			+ "Afterward, all experts will proceed to the next step together.\n"
			+ "If any expert realizes they are wrong at any point, they leave the discussion.\n\n"

			+ "THE QUESTION IS: Determine if the article is related to a specific stock.\n\n"

			+ "### How to Determine Relevance to the Stock\n\n"

			+ "**Step 1: Evaluate Relevance Based on Summarized Content**\n"
			+ "- If the article only briefly mentions the theme without providing detailed or actionable context, classify it as 'IRRELEVANT.'\n"
			+ "- If the article does not mention or is unrelated to the specific stock name provided in the question, classify it as 'IRRELEVANT.'\n"
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
			+ "  3. Future events that are highly likely to impact the stock's performance, such as announced product launches, upcoming partnerships, or significant market expansions.\n"
			+ "- If the reasons are unclear, speculative, or unrelated, classify it as 'IRRELEVANT.'\n\n"

			+ "**Final Decision**\n"
			+ "- If all steps conclude the content as 'RELEVANT,' return TRUE.\n"
			+ "- If any step classifies the content as 'IRRELEVANT,' return FALSE.\n";
}
