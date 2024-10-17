package com.bjcareer.search.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bjcareer.search.application.search.ConverterKeywordCountService;
import com.bjcareer.search.domain.AbsoluteRankKeyword;
import com.bjcareer.search.out.api.dto.DataLabTrendRequestDTO;
import com.bjcareer.search.out.api.dto.DataLabTrendResponseDTO;
import com.bjcareer.search.out.api.dto.KeywordResponseDTO;
import com.bjcareer.search.out.api.dto.KeywordResponseDTO.KeywordDto;
import com.bjcareer.search.out.api.naver.ApiAdkeywordAdapter;
import com.bjcareer.search.out.api.naver.ApiDatalabTrendAdapter;
import com.bjcareer.search.out.data.MockingData;
import com.bjcareer.search.application.exceptions.HttpCommunicationException;

class ConverterSearchCountServiceTest {

	@Mock
	private ApiDatalabTrendAdapter apiDatalabTrend;

	@Mock
	private ApiAdkeywordAdapter apiAdkeywordAdapter;

	@InjectMocks
	private ConverterKeywordCountService converterSearchCountService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAbsoluteValueOfKeyword_Success() {
		// Given
		String keyword = "example";

		// DataLabTrendResponseDTO mock data
		DataLabTrendResponseDTO.Result trendResult = MockingData.getTrendResult(keyword);
		DataLabTrendResponseDTO dataLabTrendResponseDTO = new DataLabTrendResponseDTO();
		dataLabTrendResponseDTO.setResults(List.of(trendResult));

		// KeywordResponseDTO mock data
		KeywordDto keywordCountResult = MockingData.getKeywordCountResult(keyword);
		KeywordResponseDTO keywordResponseDTO = new KeywordResponseDTO();
		keywordResponseDTO.setKeywordList(List.of(keywordCountResult));

		// Mocking external APIs
		when(apiDatalabTrend.fetchTrends(any(DataLabTrendRequestDTO.class))).thenReturn(
			Optional.of(dataLabTrendResponseDTO));
		when(apiAdkeywordAdapter.getKeywordsCount(anyString())).thenReturn(Optional.of(keywordResponseDTO));

		// When
		List<AbsoluteRankKeyword> result = converterSearchCountService.getAbsoluteValueOfKeyword(keyword);

		assertNotNull(result);
		assertFalse(result.isEmpty());
	}

	@Test
	void testGetAbsoluteValueOfKeyword_Failure_NoData() {
		// Given
		String keyword = "example";

		when(apiDatalabTrend.fetchTrends(any(DataLabTrendRequestDTO.class))).thenReturn(Optional.empty());
		when(apiAdkeywordAdapter.getKeywordsCount(anyString())).thenReturn(Optional.empty());

		// When & Then
		assertThrows(HttpCommunicationException.class, () -> {
			converterSearchCountService.getAbsoluteValueOfKeyword(keyword);
		});
	}
}
