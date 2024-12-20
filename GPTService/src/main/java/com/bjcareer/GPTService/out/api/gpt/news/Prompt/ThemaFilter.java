package com.bjcareer.GPTService.out.api.gpt.news.Prompt;

public class ThemaFilter {
	public static final String PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down one step of their thought process\n"
			+ "and then share it with the group.\n"
			+ "Next, all experts proceed to the next step, and so on.\n"
			+ "If any expert realizes they are wrong at any point, they leave.\n"
			+ "The Question is to classify themes and related stocks based on the article.\n\n"

			+ "The question is: Determine whether the cause of the stock price increase applies only to the specific company mentioned in the article.\n"

			+ "Instructions:\n"
			+ "1. If the article mentions factors such as internal announcements, earnings reports, patents, mergers, or company-specific events, classify it as 'false' (single-company factor).\n"
			+ "   Example: 'Company A's earnings grew by 20%%, resulting in a stock price increase.' → false\n\n"

			+ "2. If the article highlights political, economic, or social events that affect multiple companies, or uses terms like 'related stocks,' classify it as 'true' (theme).\n"
			+ "   Example: 'Electric vehicle demand surges, boosting related stocks including Company A, B, and C.' → true\n\n"

			+ "3. Key decision criteria:\n"
			+ "   - Does the article mention multiple companies impacted by a common factor?\n"
			+ "   - Are external macroeconomic or industry-wide events driving the movement?\n"
			+ "   - Does the article explicitly reference 'related stocks' or thematic groups?\n\n"

			+ "4. 결과에 대한 이유를 알려주세요\n"
			+ " true라면 왜 특정 기업에만 해당되는 내용인지와 false라면 왜 다수의 기업에 영향을 미치는 내용인지를 설명해주세요.\n"

			+ "Your output should be 'false' for single-company factors and 'true' for broader impacts.\n"
			+ "Process the following input:\n\n"
			+ "The question is: 'Does the article describe a stock price movement caused by factors specific to a single company, or is it related to a broader theme involving multiple companies?'\n";
}
