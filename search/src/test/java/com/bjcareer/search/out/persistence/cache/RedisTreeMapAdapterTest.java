package com.bjcareer.search.out.persistence.cache;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.entity.Stock;

@SpringBootTest
class RedisTreeMapAdapterTest {
	@Autowired
	RedissonClient redissonClient;

	@Test
	void test_중복이_제거되는지_테스트() {
		RMap<String, Object> map = redissonClient.getMap("MARKET_MAP:TEST");

		Stock stock = new Stock("0001", "삼성전자");
		Stock stock2 = new Stock("0001", "삼성전자");

		map.put(stock.getCode(), stock);
		map.put(stock2.getCode(), stock2);

		Set<String> keys =
			map.keySet();

		assertEquals(1, keys.size());
	}
}
