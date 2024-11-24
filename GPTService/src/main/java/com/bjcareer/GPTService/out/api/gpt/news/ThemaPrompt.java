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

			+ "Step 1: **Identify the Primary Reason**\n"
			+ "- Extract the main reason for the stock's increase from the <reason> field in the article.\n"
			+ "- Choose the most specific and direct reason based on the following criteria:\n"
			+ "  - Evaluate if the event or factor clearly explains the stock's rise.\n"
			+ "  - Replace overly general reasons (e.g., 'economic growth') with more specific ones (e.g., 'government fiscal policy').\n"
			+ "- Use the identified reason to proceed to the next step.\n\n"

			+ "Step 3: **Ensure the Theme and Cause are Clear and Consistent**\n"
			+ "- The theme must represent a single main concept (e.g., industry, technology, policy).\n"
			+ "- The cause should describe the main factor affecting the theme, such as an event, actor, or action.\n"
			+ "  - Example: 'Ukraine Reconstruction'\n"
			+ "    - Theme: 'Ukraine Reconstruction'\n"
			+ "    - Cause: 'Agricultural Contract'\n"
			+ "  - Example: 'The government announces renewable energy subsidies'\n"
			+ "    - Theme: 'Renewable Energy'\n"
			+ "    - Cause: 'Government Announcement'\n"
			+ "- If the cause is the event itself, use the event name as the theme and leave the cause blank.\n"
			+ "- Proceed with the identified theme and cause to the next step.\n\n"

			+ "Step 4: **Define the Output Format**\n"
			+ "- Output the theme and cause in the following format:\n"
			+ "  - Theme: [Theme Name]\n"
			+ "  - Cause: [Cause Name]\n"
			+ "- The theme and cause must be written in Korean. If the original text is in another language, translate it appropriately.\n"
			+ "- Dates must strictly follow the YYYY-MM-DD format.\n";
}
