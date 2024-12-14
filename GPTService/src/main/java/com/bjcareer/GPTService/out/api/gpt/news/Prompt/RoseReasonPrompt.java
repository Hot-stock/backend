package com.bjcareer.GPTService.out.api.gpt.news.Prompt;

public class RoseReasonPrompt {
	public static final String PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down one step of their thought process\n"
			+ "and then share it with the group.\n"
			+ "Next, all experts proceed to the next step, and so on.\n"
			+ "If any expert realizes they are wrong at any point, they leave.\n"
			+ "The content of a news article is provided within <article> </article> XML tags.\n"
			+ "The task is to summarize the news in three lines, focusing on why the stock name mentioned within <stock> </stock> might have risen.\n\n"

			+ "The Question is Based on the content provided in the article, summarize why the stock specified within the <stock> tags has risen"

			+ "Step 1: **Extract Stock Name and Remove XML Tags**\n"
			+ "- Retrieve the Stock name from within the <stock> </stock> XML tags.\n"
			+ "- Remove the XML tags within <article> </article>.\n\n"

			+ "Step 2: **Summarize the Content**\n"
			+ "- Summarize the content of the article provided within <article> </article>, focusing on the stock name.\n"
			+ "- Include details relevant to why the stock name might have risen.\n"
			+ "- Avoid speculative language and focus only on explicitly mentioned reasons.\n"
			+ "- Complete the summary and finalize the response.\n\n"

			+ "Step 3: **Check for Explicit Reasons**\n"
			+ "- Validate if the summarized reasons are explicitly supported by the article content.\n"
			+ "- Discard speculative or inferred reasons unless backed by clear evidence in the article.\n"
			+ "- Refine the summary to ensure it aligns strictly with the provided facts.\n\n"

			+ "Step 4: **Focus on Stock-Specific Reasons**\n"
			+ "답변은 반듯이 한글로 해줘\n"
			+ "- Ensure the summary answers why the stock name mentioned within <stock> </stock> might have risen.\n"
			+ "- Highlight only the reasons directly tied to the stock's performance as described in the article.\n"
			+ "- Eliminate any unrelated or tangential information to maintain focus.";
}
