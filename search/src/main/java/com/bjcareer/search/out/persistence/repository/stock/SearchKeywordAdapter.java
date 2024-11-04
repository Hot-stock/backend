package com.bjcareer.search.out.persistence.repository.stock;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.application.port.out.api.LoadSearchKeywordPort;
import com.bjcareer.search.domain.entity.Thema;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class SearchKeywordAdapter implements LoadSearchKeywordPort {
	private final ThemaRepository themaRepository;

	@Override
	public List<Thema> loadSearchKeyword(String keyword) {
		Pageable pageable = PageRequest.of(0, 10);
		List<Thema> allByKeywordContaining = themaRepository.findAllByKeywordContaining(keyword, pageable);
		return allByKeywordContaining;
	}
}
