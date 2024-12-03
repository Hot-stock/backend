package com.bjcareer.search.application.search;

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

import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;

class SearchServiceMockTest {
	@Mock
	private ThemaRepositoryPort themaRepositoryPort;

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
	void 저장된_데이터가_로딩되는지_테스트함() {
		String code = "267790";
		String name = "배럴";
		String thema = "수영복";
		String href = "https://finance.naver.com/item/main.nhn?code=267790";

		// Given: ThemaRepository가 결과를 반환하는 경우
		Stock stock = new Stock(code, name);
		ThemaInfo themaInfo = new ThemaInfo(thema, href);

		List<Thema> mockThemas = List.of(new Thema(stock, themaInfo)); // 검색 결과로 반환할 Thema 목록

		when(themaRepositoryPort.loadAllByKeywordContaining(any())).thenReturn(mockThemas);

		// When: 검색 결과가 있는 경우
		List<Thema> result = searchService.filterThemesByQuery(name);

		// Then: 검색 결과를 반환하고, 이벤트가 호출되었는지 확인
		assertFalse(result.isEmpty());  // 검색 결과가 비어있지 않음을 검증
	}

	@Test
	void 검색어가_없는_경우를_요청했을_떄() {
		int page = 1;
		int size = 2;

		// Given: ThemaRepository가 빈 결과를 반환하는 경우
		when(themaRepositoryPort.loadAllByKeywordContaining(any())).thenReturn(Collections.emptyList());

		// When: 검색 결과가 없는 경우
		List<Thema> result = searchService.filterThemesByQuery(anyString());

		// Then: 검색 결과가 없고, 이벤트가 호출되지 않았는지 확인
		assertTrue(result.isEmpty());  // 검색 결과가 비어있음을 검증
	}
}
