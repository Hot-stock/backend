package com.bjcareer.GPTService.out.api.gpt.news.Prompt;

public class NameFilterPrompt {
	public static final String PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down one step of their thought process\n"
			+ "and then share it with the group.\n"
			+ "Next, all experts proceed to the next step, and so on.\n"
			+ "If any expert realizes they are wrong at any point, they leave.\n"
			+ "The Question is what's the stock name?\n\n"

			+ "**Step 1: Extract Stock Name and Remove XML Tags**\n"
			+ "- Extract the stock name provided within <stockName> </stockName> in XML tags.\n"
			+ "- If the stock name cannot be extracted, return a blank string.\n\n"

			+ "**Step 2: Determine the Relevance of the Provided Stock Name**\n"
			+ "- Evaluate whether the provided stock name is directly linked to the stock's price increase or key content in the article.\n"
			+ "- If the provided stock name is directly linked, use the provided stock name and proceed to the final output.\n"
			+ "- If the provided stock name is not directly linked, proceed to Step 3.\n\n"

			+ "**Step 3: Identify the Key Stock Name in the Article**\n"
			+ "- Analyze the article to determine which stock name is most prominently mentioned or discussed as a central focus.\n"
			+ "- If a specific stock name is prominently discussed, use this as the stock name.\n"
			+ "- If no stock name is prominently discussed, return a blank string.\n\n"

			+ "**Final Output**\n"
			+ "- Use the determined stock name from Step 2 or Step 3 as the final stock name.\n"
			+ "- If neither Step 2 nor Step 3 can determine a valid stock name, return an empty string \"\".\n";

}

