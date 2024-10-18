package com.bjcareer.search.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;

import com.bjcareer.search.application.search.SearchService;
import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.persistence.repository.noSQL.DocumentRepository;
import com.bjcareer.search.out.persistence.repository.stock.ThemaRepository;

class SearchServiceTest {

	public static final String CODE = "267790";
	public static final String NAME = "Barrel";
	public static final String THEMA_NAME = "Thema1";
	public static final String USER_CREATED = "USER CREATED";
	@Mock
	private ThemaRepository themaRepository;

	@Mock
	private DocumentRepository documentRepository;

	@Mock
	private Trie trie;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@InjectMocks
	private SearchService searchService;

	@BeforeEach
	void setUp() {
		// Mockito의 애노테이션을 사용한 객체 초기화
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetSearchResultWithResults() {
		// Given: ThemaRepository가 결과를 반환하는 경우
		Stock stock = new Stock(CODE, NAME);
		ThemaInfo themaInfo = new ThemaInfo(THEMA_NAME, USER_CREATED);

		List<Thema> mockThemas = List.of(new Thema(stock, themaInfo)); // 검색 결과로 반환할 Thema 목록
		when(themaRepository.findAllByKeywordContaining(anyString(), any(Pageable.class))).thenReturn(mockThemas);

		// When: 검색 결과가 있는 경우
		List<Thema> result = searchService.getSearchResult(CODE, 0, 10);

		// Then: 검색 결과를 반환하고, 이벤트가 호출되었는지 확인
		assertFalse(result.isEmpty());  // 검색 결과가 비어있지 않음을 검증
		verify(eventPublisher, times(1)).publishEvent(CODE);  // 이벤트가 호출되었는지 검증
	}

	@Test
	void testGetSearchResultWithNoResults() {
		// Given: ThemaRepository가 빈 결과를 반환하는 경우
		when(themaRepository.findAllByKeywordContaining(anyString(), any(Pageable.class))).thenReturn(
			Collections.emptyList());

		// When: 검색 결과가 없는 경우
		List<Thema> result = searchService.getSearchResult(anyString(), anyInt(), anyInt());

		// Then: 검색 결과가 없고, 이벤트가 호출되지 않았는지 확인
		assertTrue(result.isEmpty());  // 검색 결과가 비어있음을 검증
		verify(eventPublisher, never()).publishEvent(anyString());  // 이벤트가 호출되지 않았는지 검증
	}
}
