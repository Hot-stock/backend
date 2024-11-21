package com.bjcareer.search.out.api.gpt.thema;

public class RelatedFilterPrompt {
	public static final String PROMPT =
		"Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will contribute one step of their thought process,\n"
			+ "iteratively refining the analysis through group discussion.\n"
			+ "If an error is identified in any reasoning, it will be corrected or excluded from the discussion.\n"
			+ "The question is as follows:\n\n"

			+ "### How to Determine Relevance to the Thema\n"

			+ "**Step 1: Evaluate Relevance Based on Summarized Content**\n"
			+ "- In cases where the article does not focus on the thema and only briefly mentions it, the article is classified as IRRELEVANT'\n"
			+ "- Analyze the summarized content to determine if it is related to the thema.\n"
			+ "- If deemed relevant, proceed to the next step; otherwise, classify it as 'IRRELEVANT.'\n\n"

			+ "**Step 2: Assess the Purpose of the Article**\n"
			+ "- If the summarized content is primarily aimed at promoting a specific product or service rather than providing relevant information about the thema, classify it as 'IRRELEVANT.'\n"
			+ "- If the content only mentions the impact of the thema without addressing its causes or essential context, classify it as 'IRRELEVANT.'\n"
			+ "- If deemed relevant, proceed to the next step.\n\n"

			+ "**Step 3: Evaluate the Scope of Impact**\n"
			+ "- If the content is determined to have a nationwide impact, classify it as 'RELEVANT.'\n"
			+ "- If the scope of impact is limited to local economies, classify it as 'IRRELEVANT.'\n"
			+ "- If the thema's influence is restricted to a regional level, classify it as 'IRRELEVANT.'\n"
			+ "- Proceed to the final decision step.\n\n"


			+ "**Final Decision**\n"
			+ "- If all steps conclude the content as 'RELEVANT,' return TRUE.\n"
			+ "- If any step classifies the content as 'IRRELEVANT,' return FALSE.\n";
}
