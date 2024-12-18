package com.bjcareer.search.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.TreeMapDomain;

@SpringBootTest
class TreeMapServiceTest {
	@Autowired
	TreeMapService treeMapService;

	@Test
	void testCalcHitMap() {
		Integer avgDay = 3;
		List<TreeMapDomain> treeMapDomains =
			treeMapService.calcTreeMap(avgDay);

		assertTrue(!treeMapDomains.isEmpty(), "히트맵이 구성되어야 한다.");
	}
}
