package com.bjcareer.GPTService.out.api.gpt.news;

public class ThemaPrompt {
	public static final String THEMA_PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down one step of their thinking,\n"
			+ "then share it with the group.\n"
			+ "Then all experts will go on to the next step, etc.\n"
			+ "If any expert realizes they're wrong at any point, then they leave.\n"
			+ "The content of an article by a news reporter is provided within <article> </article> XML tags.\n"
			+ "THE QUESTION IS: I want to extract and catalog the theme representing the reason for the stock's increase.\n"
			+ "Note: It is possible that no valid theme is found in some cases, as themes are not always present.\n\n"

			+ "Step 0: **Check News Relevance**\n"
			+ "- Before extracting themes, determine if the news is relevant to the stock name provided.\n"
			+ "- If the news is evaluated as 'IRRELEVANT,' skip all subsequent steps and return an empty theme object:\n"
			+ "  - Theme: \"\"\n"
			+ "  - Cause: \"\"\n\n"

			+ "Step 1: **Identify the Primary Reason**\n"
			+ "- Extract the main reason for the stock's increase from the <reason> field in the article.\n"
			+ "- Choose the most specific and direct reason based on the following criteria:\n"
			+ "  - Evaluate if the event or factor clearly explains the stock's rise.\n"
			+ "  - Replace overly general reasons (e.g., 'economic growth') with more specific ones (e.g., 'government fiscal policy').\n"
			+ "- Use the identified reason to proceed to the next step.\n\n"

			+ "Step 2: **Exclude Internal Company Strategies as Themes**\n"
			+ "- Internal strategies specific to one company (e.g., succession plans, internal restructuring) cannot be a theme.\n"
			+ "- Such events should only be treated as causes.\n"
			+ "- Example:\n"
			+ "  - 'Kim Seung-youn is strengthening management succession through equity purchases.' →\n"
			+ "    - Theme: None\n"
			+ "    - Cause: Hanwha Energy and Hanwha Corp. equity purchase.\n"
			+ "- Proceed to the next step if a valid theme exists.\n\n"

			+ "Step 3: **Extract the Theme Based on Industry, Policy, or Technology**\n"
			+ "- The theme must represent a broader concept such as industry trends, policy changes, or technology advancements.\n"
			+ "- If the event spans multiple industries or projects, prioritize the overarching theme.\n"
			+ "- Examples:\n"
			+ "  - 'Post-war reconstruction projects in Ukraine' → Theme: 'Ukraine Reconstruction'\n"
			+ "  - 'Government announces renewable energy subsidies' → Theme: 'Renewable Energy'\n"
			+ "- If no valid theme can be extracted, proceed to Step 4.\n\n"

			+ "Step 4: **Ensure Theme and Cause Consistency**\n"
			+ "- Themes must represent a general trend, while causes describe the specific factor affecting the theme.\n"
			+ "- If the cause is tied to internal company strategies, the theme must reflect external industry or market trends.\n"
			+ "- Examples:\n"
			+ "  - Event: 'Hanwha strengthens its energy dominance through equity purchases.'\n"
			+ "    - Theme: 'Renewable Energy'\n"
			+ "    - Cause: Equity purchase in Hanwha Energy.\n"
			+ "- If no theme is valid or relevant, proceed to Step 5 to handle empty themes.\n\n"

			+ "Step 5: **Define the Output Format**\n"
			+ "- Output the theme and cause in the following format:\n"
			+ "  - Theme: [Theme Name]\n"
			+ "  - Cause: [Cause Name]\n"
			+ "- If no theme exists, use an empty object (e.g., Theme: \"\", Cause: \"\").\n"
			+ "- The theme and cause must be written in Korean. If the original text is in another language, translate it appropriately.\n"
			+ "- Dates must strictly follow the YYYY-MM-DD format.\n";
}

