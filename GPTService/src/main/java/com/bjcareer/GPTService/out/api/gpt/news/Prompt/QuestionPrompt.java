package com.bjcareer.GPTService.out.api.gpt.news.Prompt;

import java.time.LocalDate;

import com.bjcareer.GPTService.config.AppConfig;

public class QuestionPrompt {
	public static String QUESTION_FORMAT =
		"이 뉴스의 발행일은 %s 입니다.\n"
			+ "오늘의 날짜는 " + LocalDate.now(AppConfig.ZONE_ID) + "입니다.\n"
			+ "주식 이름: <stock> %s </stock>\n"
			+ "기사 내용: <article> %s </article>\n"

			+ "Imagine a panel of three experts collaboratively addressing this question.\n"
			+ "Each expert will contribute one step of their thought process,\n"
			+ "iteratively refining the analysis through group discussion.\n"
			+ "If an error is identified in any reasoning, it will be corrected or excluded from the discussion.\n"
			+ "The question is as follows:\n\n"

			+ "<question>"
			+ "1. Summarize the article.\n"
			+ "2. Determine if the article is related to the stock.\n"
			+ "3. Pinpoint the next event mentioned in the article and explain its significance.\n"
			+ "</question>\n\n"

			+ "### Steps to Summarize article\n"
			+ "Step 1: **Extract Thema Name and Remove XML Tags**\n"
			+ "- Retrieve the thema name from within the <stock> </stock> XML tags.\n"
			+ "- Remove the XML tags within <article> </article>.\n"

			+ "Step 2: **Summarize the Content**\n"
			+ "- Summarize the content of the article provided within <article> </article>, focusing on the stock name.\n"
			+ "- Complete the summary and finalize the response.\n"

			+ "THE QUESTION IS: Determine if the article is related to a specific stock.\n\n"

			+ "### How to Determine Relevance to the Stock\n\n"

			+ "**Step 1: Initial Relevance Check**\n"
			+ "- If the article does not mention or is unrelated to the specific stock name provided in the question, classify it as 'IRRELEVANT.'\n"
			+ "- If the article only briefly mentions the stock name or theme without providing detailed or actionable context, classify it as 'IRRELEVANT.'\n"
			+ "- If the article mentions only the stock's performance (e.g., hitting upper limit or percentage increase) without explaining the underlying reasons, classify it as 'IRRELEVANT.'\n"
			+ "- If deemed relevant, proceed to the next step; otherwise, classify it as 'IRRELEVANT.'\n\n"

			+ "**Step 2: Assess the Purpose of the Article**\n"
			+ "- Based on the summarized content, evaluate whether the article clearly explains the reason for the stock's increase or performance.\n"
			+ "- Classify the article as 'IRRELEVANT' in the following cases:\n"
			+ "  1. If the explanation relies solely on financial metrics (e.g., past performance, generic financial analysis) that are not direct causes of the increase.\n"
			+ "  2. If it fails to provide clear external or internal reasons for the increase, such as significant investor activities, economic policies, or market events.\n"
			+ "  3. If the article mentions internal company strategies (e.g., restructuring, leadership changes) but lacks external validation or broader market implications, classify it as 'IRRELEVANT.'\n"
			+ "  4. If the article attributes the stock's increase to generic investor behavior (e.g., foreign or institutional buying), classify it as 'IRRELEVANT.'\n"
			+ "  5. If the article primarily discusses financial metrics (e.g., institutional net buying/selling, trading volume), classify it as 'IRRELEVANT.'\n"
			+ "  6. If the article attributes stock performance to projections or forecasts (e.g., positive outlook for revenue or profit) without specifying the source or context of the announcement, classify it as 'IRRELEVANT.'\n"
			+ "  7. If the article mentions only the stock's performance metrics (e.g., hitting upper limit or percentage increase) without providing the reasons behind the performance, classify it as 'IRRELEVANT.'\n"
			+ "- If none of the above applies, proceed to the next step.\n\n"

			+ "**Step 3: Analyze the Reasons for the Stock Increase**\n"
			+ "- Identify and explain the primary reasons for the stock's increase or performance.\n"
			+ "- Classify the article as 'RELEVANT' if the increase is linked to:\n"
			+ "  1. Corporate growth factors, such as policy changes, new product launches, or mergers and acquisitions.\n"
			+ "  2. External events or conditions directly impacting the stock's performance, such as competitor disruptions, government subsidies, or major economic shifts.\n"
			+ "  3. Future events that are highly likely to impact the stock's performance, such as announced product launches, upcoming partnerships, or significant market expansions.\n"
			+ "- Classify the article as 'IRRELEVANT' if:\n"
			+ "  - The reasons are unclear, speculative, or unrelated to the stock's performance.\n"
			+ "  - The primary reasons do not represent actionable trends or impactful changes in the market or industry.\n"
			+ "  - The reasons rely only on investor behavior (e.g., net buying/selling) without additional external factors.\n"
			+ "  - The article only highlights stock performance metrics (e.g., hitting upper limit or percentage increase) without explaining the reasons behind the performance.\n\n"

			+ "**Final Decision**\n"
			+ "- If all steps conclude the content as 'RELEVANT,' return TRUE.\n"
			+ "- If any step classifies the content as 'IRRELEVANT,' return FALSE.\n"


			+ "### Explain the Importance of the Next Date\n"
			+ "**Step 1: Analyze the Importance of the Date**\n"
			+ "- Explain why the extracted date is important.\n"
			+ "- This step involves dividing the information into facts and opinions.\n\n"

			+ "**Fact**\n"
			+ "- Include only the information explicitly stated in the article about the date.\n"
			+ "- Example: If the date refers to a specific event (e.g., 'shareholder meeting,' 'new product launch,' 'policy implementation date'), explain what is explicitly stated.\n"
			+ "- Provide concrete data based on the article, quoting or paraphrasing directly from it.\n\n"

			+ "**Opinion**\n"
			+ "- Provide reasonable interpretations based on the content of the article.\n"
			+ "- Example: 'The stock price may rise on this date,' or 'This event is likely to attract investor attention.'\n"
			+ "- Add context or extrapolate logical conclusions beyond what is directly mentioned in the article.\n\n"

			+ "**Example**\n"
			+ "- Fact: 'According to the article, December 1, 2024, is the date when a major policy change will be implemented.'\n"
			+ "- Opinion: 'This policy change is expected to drive growth in the relevant industry and attract investor interest.'\n\n"

			+ "### Output Requirements\n"
			+ "- The response must be written in Korean.\n";
}
