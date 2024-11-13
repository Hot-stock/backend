package com.bjcareer.search.out.api.gpt.news;

import com.bjcareer.search.out.api.gpt.PropertyArrayObject;
import com.bjcareer.search.out.api.gpt.PropertyDetail;
import com.bjcareer.search.out.api.gpt.PropertyObject;

//한글보다 영어로가 더 좋다
//대문자로 쓰면 더 좋다.
//4o가 더 정확한 추론을 진행한다
public class NewsProperties {
	public static final String[] required = {"filtered", "name", "reason", "themas", "next", "next_reason"};
	// Filtered: Determines if the article is fake news

	//for 4o
	public PropertyDetail filtered = new PropertyDetail("boolean",
		"The content of an article written by a news reporter is provided within <article> </article> XML tags.\n"
			+ "Step 1: If the content within <article> </article> is related to <stockName> </stockName>, proceed to the next step. If unrelated, it will be classified as FAKE NEWS.\n"
			+ "Step 2: Summarize the content within <article> </article> and proceed to the next step.\n"
			+ "Step 3: Determine if the summarized content clearly explains the reason for the stock's increase. The rules are as follows:\n"
			+ "3-1: The first criterion is to identify any irrational explanations for the stock's increase as unclear. For example, indirect factors like 'large purchases,' 'psychological factors,' 'chart patterns,' 'volume increase,' 'investor interest,' or 'general market interest' will be classified as unclear and deemed as FAKE NEWS.\n"
			+ "3-2: News indicating that an <IMPORTANT>EVENT OCCURRED AND THAT THE STOCK ROSE BASED ON THAT EVENT WILL BE DEEMED CLEAR NEWS. 예를들면 분쟁이나, 전쟁, 정치인의 발언으로 인해서 상승을 했다라고 하면 CLEAR NEWS HOWEVER, EVENTS INVOLVING THE STOCK ITSELF, LIKE REACHING THE UPPER LIMIT, WILL BE CLASSIFIED AS UNCLEAR, AS WELL AS ANY INFORMATION FOLLOWING PURCHASES, SUCH AS 'FOREIGN INVESTORS BOUGHT SHARES.'</IMPORTANT> \n"
			+ "3-3: Any <IMPORTANT>UNPREDICTABLE FUTURE OCCURRENCES WILL ALSO BE CLASSIFIED AS UNCLEAR, SUCH AS 'BONUS ISSUES' OR 'SHAREHOLDING DISCLOSURES,' WHICH LACK A PREDICTABLE SCHEDULE.</IMPORTANT>\n"
			+ "3-4: If deemed unclear, it will be FAKE NEWS; if clear, it will be REAL NEWS.\n"
			+ "Step 4: The second criterion is financial grounds. If the explanation is based on 'strong financials' or other stocks in the same sector performing well, it will be deemed unclear. Unclear news will be FAKE NEWS, and clear news will be REAL NEWS.\n"
			+ "Portions marked as <IMPORTANT></IMPORTANT> should think deeply and solve the issue.\n"
			+ "IF THE NEWS IS FAKE NEWS, RETURN TRUE; IF REAL NEWS, RETURN FALSE."
	);

	// Name: Provides the exact stock name
	public PropertyDetail name = new PropertyDetail("string",
		"Overview: Provide the exact name of the stock that has experienced a rise in price.\n"
	);

	// Reason: Brief summary for the stock's rise reason
	public PropertyDetail reason = new PropertyDetail("string",
		"Overview: Summarize the reason for the stock’s rise in up to 3 concise lines.\n" +
			"Focus:\n" +
			"- Emphasize specific connections to events, key announcements, or influential individuals."
	);

	// 테마: 주가 상승과 관련된 주요 테마를 식별합니다.
	public PropertyArrayObject themas = new PropertyArrayObject(
		"I will extract and catalog the theme representing the reason for the stock's increase.\n"
			+ "Step 1: First, refer to the reason field to gather the primary reason for the stock's increase.\n"
			+ "Step 2: Extract the name of the event that triggered the stock’s increase.\n"
			+ "   - Example 1: If Tesla's stock rose because of a new supercomputer, Tesla is the event, and the reason is the supercomputer.\n"
			+ "   - Example 2: If an individual’s action caused the event, such as Trump's statement leading to a stock increase, Trump becomes the event and the reason is the statement.\n"
			+ "   - Example 3: For themes like a push for resource production within a country, 'domestic' or 'resource' would be the event, and 'production push' the reason.\n"
			+ "Step 3: The most important rule is that the event name must **be specific and consist of only one word**.\n"
			+ "   - For instance, instead of 'trade dispute,' use 'US-China trade dispute' to ensure specificity.\n"
			+ "뉴스에 나온 단어를 사용하는 전략으로 변경하고, 반듯이 한글로 번역해야한다.\n"
			+ "Think deeply and solve the issue."
		,
		new PropertyObject(new ThemaVariable(), ThemaVariable.required)
	);


	// Next: Closest significant date relevant to the theme
	public PropertyDetail next = new PropertyDetail("string",
		"Overview: Provide the closest significant date explicitly mentioned in the article related to the stock’s theme.\n"
			+
			"Format:\n" +
			"- Date format: YYYY-MM-DD.\n" +
			"- Only include dates that are precisely and explicitly stated in the article.\n" +
			"- If no specific date is available, leave this field empty."
	);

	// Next Reason: Explanation for the selected date in "next"
	public PropertyDetail next_reason = new PropertyDetail("string",
		"Overview: Provide a concise explanation for the date in 'next', detailing its relevance to the theme.\n" +
			"Details:\n" +
			"- Explain the connection to the theme.\n" +
			"- Include only direct information from the article without inference.\n" +
			"- If no date is in 'next', set this field to null."
	);
}
