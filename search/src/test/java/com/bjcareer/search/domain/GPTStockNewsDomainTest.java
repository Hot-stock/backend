package com.bjcareer.search.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

class GPTStockNewsDomainTest {

	@Test
	void test_날짜변환(){
		LocalDate answer = LocalDate.parse("2024-10-01");
		GPTStockNewsDomain GPTStockNewsDomain = new GPTStockNewsDomain("그린리소스", "...", new ArrayList<>(), "2024-10-01", "다음이유");
		assertEquals(answer, GPTStockNewsDomain.getNext().get());
	}
}
