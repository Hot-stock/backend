package com.bjcareer.search.out.api.gpt.thema;

public class NextSchedulePrompt {
	public static final String PROMPT =
		"Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will document one step of their thought process\n"
			+ "and then share it with the group for discussion.\n"
			+ "The experts will proceed to the next step together, iteratively refining their analysis.\n"
			+ "If any expert identifies an error in their reasoning, they will withdraw from the discussion.\n"
			+ "The question is as follows:\n\n"

			+ "### Steps to Find the Next Event\n"
			+ "**Step 1: Extract Closest Significant Future Date**\n"
			+ "- Provide the closest significant future date explicitly mentioned in the article related to the stock’s theme.\n"
			+ "- The extracted date must strictly be after the news publication date (today).\n"
			+ "- Date format: YYYY-MM-DD.\n"
			+ "- If no future date is mentioned or all mentioned dates are before or equal to today, leave this field empty.\n\n";
}
