package com.bjcareer.search.application.search;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.entity.Thema;

@SpringBootTest
class SearchServiceTest {
	@Autowired
	SearchService searchService;

	@Test
	void 주어진_키워드로_검색결과를_반환하는지() {
		//given
		String keyword = "중국";
		int page = 0;
		int size = 10;

		//when
		List<Thema> searchResult = searchService.getSearchResult(keyword, page, size);

		//then
		assertFalse(searchResult.isEmpty());
	}
}
