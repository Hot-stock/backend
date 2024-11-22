package com.bjcareer.GPTService.out.api.gpt.insight;

import com.bjcareer.GPTService.out.api.gpt.PropertyDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class KeyDateVariable {
	@JsonIgnore
	public static final String[] required = {"date", "reason"};

	public PropertyDetail date = new PropertyDetail("string", "YYYY-MM-DD");
	public PropertyDetail reason = new PropertyDetail("string",
		"Explain why each date is significant based on historical or expected events, such as policy announcements or earnings. Ensure dates are relevant for substantial stock movement.");
}
