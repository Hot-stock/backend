package com.bjcareer.search.application.port.out;

import java.util.List;

import com.bjcareer.search.domain.entity.Thema;

public interface LoadSearchKeywordPort {
	List<Thema> loadSearchKeyword(String keyword);
}