package com.bjcareer.search.out.data;

import java.util.List;

import com.bjcareer.search.out.api.dto.DataLabTrendResponseDTO;
import com.bjcareer.search.out.api.dto.KeywordResponseDTO;

public class MockingData {
	public static DataLabTrendResponseDTO.Result getTrendResult(String keyword) {
		return new DataLabTrendResponseDTO.Result(
			keyword,
			List.of(keyword),
			List.of(
				new DataLabTrendResponseDTO.Result.Info("2023-01-01", 30.0),
				new DataLabTrendResponseDTO.Result.Info("2023-02-01", 31.0)
			)
		);
	}

	public static KeywordResponseDTO.KeywordDto getKeywordCountResult(String keyword) {
		return new KeywordResponseDTO.KeywordDto(keyword, "1000", "2000", 300.5, 150.3, 0.02, 0.03, 10, "중간"
		);
	}
}
