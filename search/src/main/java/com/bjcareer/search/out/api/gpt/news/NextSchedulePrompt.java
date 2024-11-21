package com.bjcareer.search.out.api.gpt.news;

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
			+ "- Provide the most important significant future date explicitly mentioned in the article related to the theme.\n"
			+ "- The extracted date must strictly be after the news publication date (today).\n"
			+ "- Date format: YYYY-MM-DD.\n"
			+ "- If no future date is mentioned or all mentioned dates are before or equal to today, leave this field empty.\n\n"

			+ "**Step 2: Identify Date Related to Thema**\n"
			+ "- Review the content to determine if there is a date explicitly tied to the 자율주행.\n"
			+ "- A date is related to the thema if:\n"
			+ "  - It directly involves an event, announcement, or activity relevant to the thema.\n"
			+ "  - It marks a milestone for the thema's progress, such as a policy implementation, product launch, or market shift.\n"
			+ "- If a related date is found, ensure it is explicitly tied to the thema in the article.\n"
			+ "- Date format: YYYY-MM-DD.\n"
			+ "- If no related date is found, leave this field empty.\n\n"

			+ "**Final Note**\n"
			+ "- Ensure all extracted dates are validated against the thema to ensure relevance.\n";
}
