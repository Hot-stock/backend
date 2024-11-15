package com.bjcareer.search.out.api.gpt.news;

public class QuestionPrompt {
	public static String QUESTION_FORMAT = " Today’s date is the news publication date: %s\n" +
		"Stock name is <stockname>%s</stockname>\n" +
		"Analyze the following news <article>%s</article>\n" +
		"Here is the Thema's name <themaName>%s</themaName>\n" +
		"Provide the response in Korean.";
}
