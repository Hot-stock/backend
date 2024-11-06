package com.bjcareer.search.IntegrationTest.out.api.naver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.out.api.dto.DataLabTrendRequestDTO;
import com.bjcareer.search.out.api.dto.DataLabTrendResponseDTO;
import com.bjcareer.search.out.api.naver.ApiDatalabTrendAdapter;

@SpringBootTest
class ApiDatalabTrendTest {
	@Autowired
	ApiDatalabTrendAdapter apiDatalabTrend;

	@Test
	void fetchTrends() {
		DataLabTrendRequestDTO.KeywordGroup keywordGroup = new DataLabTrendRequestDTO.KeywordGroup("코로나", new ArrayList<>(
			List.of("코로나", "코로나19", "코로나바이러스", "코로나19바이러스")));

		List<DataLabTrendRequestDTO.KeywordGroup> keywordGroups = new ArrayList<>(List.of(keywordGroup));
		DataLabTrendRequestDTO request = new DataLabTrendRequestDTO("2017-01-01", "2017-04-30", keywordGroups);
		Optional<DataLabTrendResponseDTO> dataLabTrendResponseDTO = apiDatalabTrend.fetchTrends(request);

		assertTrue(dataLabTrendResponseDTO.isPresent());
	}
}
