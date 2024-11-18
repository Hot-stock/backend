package com.bjcareer.search.application.port.out.persistence.ranking;

import java.util.List;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;

public interface MarketRankingPort {
	void updateRankingNews(GTPNewsDomain news, Stock stock);
	List<GTPNewsDomain> getRankingNews();
	boolean isExistInCache(Stock stock);
	void removeRankingNews(Stock stock);
}
