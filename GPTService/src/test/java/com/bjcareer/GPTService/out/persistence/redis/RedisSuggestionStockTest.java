package com.bjcareer.GPTService.out.persistence.redis;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisSuggestionStockTest {

	@Autowired
	RedisSuggestionStock redisSuggestionStock;

	@Test
	void test() {
		redisSuggestionStock.updateSuggestionStock("컬러레이");
	}
}
