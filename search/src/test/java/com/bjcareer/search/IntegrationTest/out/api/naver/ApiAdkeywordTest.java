package com.bjcareer.search.IntegrationTest.out.api.naver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.out.api.dto.KeywordResponseDTO;
import com.bjcareer.search.out.api.naver.ApiAdkeywordAdapter;

@SpringBootTest
class ApiAdkeywordTest {
	@Autowired
	private ApiAdkeywordAdapter apiAdkeywordAdapter;

	@Test
	void fetchAdKeywords() {
		// Given
		String keyword = "강아지";

		// When
		Optional<KeywordResponseDTO> keywordsCount = apiAdkeywordAdapter.getKeywordsCount(keyword);

		// Then
		assertTrue(keywordsCount.isPresent(), "검색 결과가 있어야 합니다.");
		KeywordResponseDTO.KeywordDto keywordDto = keywordsCount.get().getKeywordList().get(0);
		assertNotNull(keywordDto, "검색된 키워드 정보가 null이 아닙니다.");
	}
}
