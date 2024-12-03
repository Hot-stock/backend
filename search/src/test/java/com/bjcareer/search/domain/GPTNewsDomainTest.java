package com.bjcareer.search.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.bjcareer.search.domain.gpt.GPTNewsDomain;

class GPTNewsDomainTest {

	@Test
	void test_날짜변환(){
		LocalDate answer = LocalDate.parse("2024-10-01");
		GPTNewsDomain GPTNewsDomain = new GPTNewsDomain("그린리소스", "...", new ArrayList<>(), "2024-10-01", "다음이유");
		assertEquals(answer, GPTNewsDomain.getNext().get());
	}
}
