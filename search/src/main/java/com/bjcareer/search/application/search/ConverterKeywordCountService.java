package com.bjcareer.search.application.search;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bjcareer.search.application.exceptions.HttpCommunicationException;
import com.bjcareer.search.application.port.in.KeywordUsecase;
import com.bjcareer.search.application.port.out.api.NaverAdPort;
import com.bjcareer.search.application.port.out.api.NaverDataTrendPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.AbsoluteRankKeyword;
import com.bjcareer.search.out.api.dto.DataLabTrendRequestDTO;
import com.bjcareer.search.out.api.dto.DataLabTrendResponseDTO;
import com.bjcareer.search.out.api.dto.KeywordResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConverterKeywordCountService implements KeywordUsecase {
	private final NaverDataTrendPort naverDataTrendPort;
	private final NaverAdPort naverAdPort;

	public List<AbsoluteRankKeyword> getAbsoluteValueOfKeyword(String keyword) {
		LocalDateTime endDate = LocalDateTime.now(AppConfig.ZONE_ID).minusDays(1);
		LocalDateTime startDate = endDate.minusMonths(1);

		keyword = trimKeyword(keyword);
		DataLabTrendRequestDTO request = getRequestOfNaverTrend(keyword, startDate, endDate);

		Optional<DataLabTrendResponseDTO> response = naverDataTrendPort.fetchTrends(request);
		Optional<KeywordResponseDTO> keywordsCount = naverAdPort.getKeywordsCount(keyword);

		if (vaildationFetchData(response, keywordsCount))
			throw new HttpCommunicationException("Error fetching data");

		KeywordResponseDTO keywordResponseDTO = keywordsCount.get();
		DataLabTrendResponseDTO dataLabTrendResponseDTO = response.get();

		Double relativeSearchCount = getRelativeSearchCount(dataLabTrendResponseDTO);

		int totalQcCnt = keywordResponseDTO.getKeywordList().get(0).getTotalQcCnt();
		double ratio = totalQcCnt / relativeSearchCount;

		return getAbsoluteRankKeywords(dataLabTrendResponseDTO, ratio);
	}

	private Double getRelativeSearchCount(DataLabTrendResponseDTO dataLabTrendResponseDTO) {
		return dataLabTrendResponseDTO.getResults().stream()
			.flatMap(result -> result.getData().stream())
			.mapToDouble(DataLabTrendResponseDTO.Result.Info::getRatio)
			.sum();
	}

	private List<AbsoluteRankKeyword> getAbsoluteRankKeywords(DataLabTrendResponseDTO dataLabTrendResponseDTO,
		double ratio) {

		return dataLabTrendResponseDTO.getResults().stream()
			.flatMap(result -> result.getData().stream())
			.map(info -> new AbsoluteRankKeyword(info.getRatio() * ratio, info.period))
			.collect(Collectors.toList());
	}

	private boolean vaildationFetchData(Optional<DataLabTrendResponseDTO> response,
		Optional<KeywordResponseDTO> keywordsCount) {

		if (response.isEmpty() || keywordsCount.isEmpty()) {
			log.error("Error fetching data");
			return true;
		}

		return false;
	}

	private DataLabTrendRequestDTO getRequestOfNaverTrend(String keyword, LocalDateTime startDate,
		LocalDateTime endDate) {

		DataLabTrendRequestDTO.KeywordGroup keywordGroup = new DataLabTrendRequestDTO.KeywordGroup(
			keyword, List.of(keyword));

		return new DataLabTrendRequestDTO(
			startDate.toLocalDate().toString(), endDate.toLocalDate().toString(), List.of(keywordGroup));
	}

	private String trimKeyword(String keyword) {
		keyword = keyword.replaceAll("\\(.*?\\)", "").trim();
		return keyword;
	}
}
