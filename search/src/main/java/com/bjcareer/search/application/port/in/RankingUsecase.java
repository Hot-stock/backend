package com.bjcareer.search.application.port.in;

import java.util.List;

import org.springframework.data.util.Pair;

import com.bjcareer.search.domain.entity.Stock;

public interface RankingUsecase {
	void updateKeyword(String keyword);
	List<Pair<Stock, String>> getRankKeyword(Integer index);
}
