package com.bjcareer.search.repository.stock;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.ThemaInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ThemaInfoRepositoryTest {
	@Autowired ThemaInfoRepository themaInfoRepository;

	@Test
	void 정해진_테마를_불러올_수_있는지_확인() {
		List<ThemaInfo> themaInfos = themaInfoRepository.findByNameContains("출산");
		System.out.println("themaInfos = " + themaInfos);
		assertEquals(1, themaInfos.size());
	}
}