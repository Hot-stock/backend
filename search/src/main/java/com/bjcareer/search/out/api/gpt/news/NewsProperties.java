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

	// Name: Provides the exact stock name
	public PropertyDetail name = new PropertyDetail("string", NameFilterPrompt.PROMPT);

	// Reason: Brief summary for the stock's rise reason
	public PropertyDetail reason = new PropertyDetail("string", RoseReasonPrompt.PROMPT);


	//for 4o
	public PropertyDetail filtered = new PropertyDetail("boolean", NewsFilterPrompt.FILTER_PROMPT);


	// Theme: Identifies key themes related to stock increases.
	public PropertyArrayObject themas = new PropertyArrayObject(ThemaPrompt.THEMA_PROMPT,
		new PropertyObject(new ThemaVariable(), ThemaVariable.required)
	);

	public PropertyDetail next = new PropertyDetail("string",
		"Overview: Provide the closest significant **future date** explicitly mentioned in the article related to the stock’s theme.\n"
			+
			"Format:\n" +
			"- Date format: YYYY-MM-DD.\n" +
			"- Only include dates explicitly stated in the article and occurring **in the future**.\n" +
			"- If no future date is mentioned, leave this field empty."
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
