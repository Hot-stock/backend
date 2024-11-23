package com.bjcareer.GPTService.out.api.gpt.thema;

public class HistoryPattern {
	public static final String PROMPT =
		"Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will document one step of their thought process\n"
			+ "and then share it with the group for discussion.\n"
			+ "The experts will proceed to the next step together, iteratively refining their analysis.\n"
			+ "If any expert identifies an error in their reasoning, they will withdraw from the discussion.\n"
			+ "The question is as follows:\n\n"

			+ "### Steps to Find the Historical Pattern\n"
			+ "Please provide the historical pattern of the theme. Leave blank if no significant date is identified.";
}
