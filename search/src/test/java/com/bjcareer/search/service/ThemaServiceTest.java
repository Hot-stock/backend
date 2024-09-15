package com.bjcareer.search.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.entity.ThemaInfo;

@SpringBootTest
class ThemaServiceTest {
	@Autowired ThemaService service;

	@Test
	void test_search() {
		List<ThemaInfo> themaInfos = service.searchThema("출산");
		System.out.println("themaInfos = " + themaInfos);
	}

}