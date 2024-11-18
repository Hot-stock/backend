package com.bjcareer.search.out.api.gpt.news;

public class NameFilterPrompt {
	public static final String PROMPT =
		"Imagine three different experts are answering this question.\n"
			+ "All experts will write down one step of their thought process\n"
			+ "and then share it with the group.\n"
			+ "Next, all experts proceed to the next step, and so on.\n"
			+ "If any expert realizes they are wrong at any point, they leave.\n"
			+ "THe Question is what's the stock name?\n"

			+ "Step 1: **Extract Stock Name and Remove XML Tags**\n"
			+ "- Extract the stock name provided within <stockName> </stockName> in XML tags.\n"
			+ "- Can't Extract the stock name return blank string.\n";
}
