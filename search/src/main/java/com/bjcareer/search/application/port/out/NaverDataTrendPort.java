package com.bjcareer.search.application.port.out;

import java.util.Optional;

import com.bjcareer.search.out.api.dto.DataLabTrendRequestDTO;
import com.bjcareer.search.out.api.dto.DataLabTrendResponseDTO;

public interface NaverDataTrendPort {
	Optional<DataLabTrendResponseDTO> fetchTrends(DataLabTrendRequestDTO request);
}
