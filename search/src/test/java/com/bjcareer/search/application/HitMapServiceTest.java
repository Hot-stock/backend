package com.bjcareer.search.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.HitMapDomain;

@SpringBootTest
class HitMapServiceTest {
	@Autowired HitMapService hitMapService;


	@Test
	void testCalcHitMap() {
		Integer avgDay = 3;
		List<HitMapDomain> hitMapDomains =
			hitMapService.calcHitMap(avgDay);

		hitMapDomains.forEach(hitMapDomain -> {
			System.out.println("hitMapDomain = " + hitMapDomain);
		});
	}
}
