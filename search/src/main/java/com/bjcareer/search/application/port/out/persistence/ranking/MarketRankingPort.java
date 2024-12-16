package com.bjcareer.search.application.port.out.persistence.ranking;

import java.util.List;

import org.springframework.data.util.Pair;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;

public interface MarketRankingPort {
	void updateRankingNews(GPTNewsDomain news, Stock stock);
	List<Pair<String, GPTNewsDomain>> getRankingNews();
	boolean isExistInCache(Stock stock);
	void removeRankingNews(Stock stock);
}
