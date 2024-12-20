package com.bjcareer.GPTService.domain.gpt.thema;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class ThemaInfoTest {

	@Test
	void changeThemaNameUsingLevenshteinDistance() {
		ThemaInfo themaInfo = new ThemaInfo(null, "남북경협", "1234");
		themaInfo.changeThemaNameUsingLevenshteinDistance(List.of("남북 경협"));
		assertEquals("남북 경협", themaInfo.getName());
	}

}
