package com.bjcareer.search.candidate.cache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bjcareer.search.out.repository.cache.CacheRepository;
import com.bjcareer.search.out.repository.noSQL.DocumentRepository;

class CacheTrieServiceTest {

	@Mock
	private CacheRepository cacheRepository;

	@Mock
	private DocumentRepository documentRepository;

	@InjectMocks
	private CacheTrieService cacheTrieService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testUpdateKeyword() {
		// given
		String keyword = "testKeyword";
		Document document = mock(Document.class);
		List<String> keywordList = Arrays.asList("child1", "child2");

		// when
		when(documentRepository.findSingleByKeyword(keyword)).thenReturn(document);
		when(documentRepository.getkeyworkList(document)).thenReturn(keywordList);

		// then
		verify(cacheRepository, times(1)).saveKeyword(any(CacheNode.class));
		verify(documentRepository, times(1)).findSingleByKeyword(keyword);
		verify(documentRepository, times(1)).getkeyworkList(document);
	}

	@Test
	void testSearch_FoundInCache() {
		// given
		String query = "testQuery";
		CacheNode cacheNode = new CacheNode(query, Arrays.asList("child1", "child2"));

		// when
		when(cacheRepository.findByKeyword(query)).thenReturn(Optional.of(cacheNode));

		// 실행
		List<String> result = cacheTrieService.search(query);

		// then
		assertEquals(2, result.size());
		assertEquals("child1", result.get(0));
		assertEquals("child2", result.get(1));
		verify(cacheRepository).findByKeyword(query);
	}

	@Test
	void testSearch_NotFoundInCache() {
		// given
		String query = "testQuery";

		// when
		when(cacheRepository.findByKeyword(query)).thenReturn(Optional.empty());

		// 실행
		List<String> result = cacheTrieService.search(query);

		// then
		assertTrue(result.isEmpty());
		verify(cacheRepository).findByKeyword(query);
	}
}

