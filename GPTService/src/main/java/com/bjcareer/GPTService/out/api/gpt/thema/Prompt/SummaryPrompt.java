package com.bjcareer.GPTService.out.api.gpt.thema.Prompt;

public class SummaryPrompt {
	public static final String PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down one step of their thought process\n"
			+ "and then share it with the group.\n"
			+ "Next, all experts proceed to the next step, and so on.\n"
			+ "If any expert realizes they are wrong at any point, they leave.\n"
			+ "The content of a news article is provided within <article> </article> XML tags.\n"
			+ "The task is to summarize the news in three lines.\n"

			+ "Step 1: **Extract Stock Name and Remove XML Tags**\n"
			+ "- Remove the XML tags within <article> </article>.\n"

			+ "Step 2: **Summarize the Content**\n"
			+ "- Summarize the content of the article provided within <article> </article>, focusing on the thema.\n"
			+ "영향성도 평가해줘야 함\n"
			+ "- Complete the summary and finalize the response.\n";
}
