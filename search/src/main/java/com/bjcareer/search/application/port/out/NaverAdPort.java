package com.bjcareer.search.application.port.out;

import java.util.Optional;

import com.bjcareer.search.out.api.dto.KeywordResponseDTO;

public interface NaverAdPort {
	Optional<KeywordResponseDTO> getKeywordsCount(String keywords);
}
