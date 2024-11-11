package com.bjcareer.search.out.api.gpt.news;

import com.bjcareer.search.out.api.gpt.PropertyDetail;

public class ThemaVariable {
	public static final String[] required = {"name", "reason"};

	public PropertyDetail name = new PropertyDetail("string", "Construct a theme for the stock using keywords derived from the 'reason' field. Prioritize significant entities (e.g., 'Trump', 'inflation') or key issues. Use one-word keywords separated by commas, with a maximum of 3 keywords. Avoid using the stock name itself as a theme.");
	public PropertyDetail reason = new PropertyDetail("string", "State the primary reason for the stock's movement, capturing any thematic elements such as economic policies or major market influencers.");
}
