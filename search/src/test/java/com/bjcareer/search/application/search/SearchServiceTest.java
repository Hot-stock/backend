package com.bjcareer.search.application.search;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
	@Mock
	ThemaRepositoryPort themaRepositoryPort;
	@Mock
	Trie trie;

	@InjectMocks
	SearchService searchService;

	@Test
	void 주어진_키워드로__검색결과를_반환하는지() {
		//given
		String keyword = "중국";
		int page = 0;
		int size = 10;

		when(themaRepositoryPort.loadAllByKeywordContaining(any())).thenReturn(
			List.of(new Thema(new Stock("2134", "진서티이씨"), new ThemaInfo("중국"))));

		//when
		List<Thema> searchResult = searchService.filterThemesByQuery(keyword);

		//then
		assertFalse(searchResult.isEmpty());
	}
}
