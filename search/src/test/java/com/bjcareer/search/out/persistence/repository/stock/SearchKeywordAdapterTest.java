package com.bjcareer.search.out.persistence.repository.stock;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.entity.Thema;

@SpringBootTest
class SearchKeywordAdapterTest {
	@Autowired
	SearchKeywordAdapter themaRepository;

	@Test
	void 데이터_불러오는지_획인() {
		List<Thema> allByKeywordContaining = themaRepository.loadSearchKeyword("재건");
		System.out.println("allByKeywordContaining = " + allByKeywordContaining);
	}
}
