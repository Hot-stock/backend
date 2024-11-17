package com.bjcareer.search.out.api.gpt.news;

public class ThemaPrompt {
	public static final String THEMA_PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down 1 step of their thinking,\n"
			+ "then share it with the group.\n"
			+ "Then all experts will go on to the next step, etc.\n"
			+ "If any expert realizes they're wrong at any point, then they leave.\n"
			+ "The content of an article by a news reporter is provided within <article> </article> XML tags.\n"
			+ "THE QUESTION IS I want to extract and catalog the theme representing the reason for the stock's increase.\n"
			+ "Note: It is possible that no valid theme is found in some cases, as themes are not always present.\n"

			+ "Step 1: **Identify Primary Reason**\n"
			+ "- Refer to the 'name' field if name is blank then return empty object\n"
			+ "- Refer to the 'reason' field to gather the primary reason for the stock's increase.\n"
			+ "- Continue to Step 2 with the primary reason.\n"

			+ "Step 2: **Extract Event Trigger Name**\n"
			+ "- Extract the name of the event or entity that triggered the stock’s increase.\n"
			+ "   - Example 1: If Tesla's stock rose due to a new supercomputer, 'Tesla' is the event, and 'supercomputer' is the reason. Companies mentioned in the question prompt cannot be used as a theme name.\n"
			+ "   - Example 2: If an individual's action caused the event, such as Trump's statement leading to a stock increase, 'Trump' becomes the event, and 'statement' is the reason.\n"
			+ "   - Example 3: For broader themes, like a push for resource production within a country, 'domestic' or 'resource' would be the event, and 'production push' the reason.\n"

			+ "Step 3: **Ensure Event Specificity**\n"
			+ "- The event name must be **specific and consist of only one word** to ensure clarity and focus.\n"
			+ "- For example, instead of 'trade dispute,' use a more specific term like 'US-China trade dispute' for greater precision.\n"

			+ "Step 4: **Exclude Stock-Intrinsic Events**\n"
			+ "- If the event is intrinsic to the stock itself (e.g., 'IPO', 'share buyback', or 'price limit reached') never using it as a primary theme.\n"
			+ "- Stock-intrinsic events are typically internal actions that do not represent a substantial external influence.\n"

			+ "Step 5: **Use Korean for Theme and Reason**\n"
			+ "- Date MUST STRICTLY FOLLOW THE YYYY-MM-DD FORMAT!!.\n"
			+ "- Both the theme name and reason must be in Korean.\n";
}
