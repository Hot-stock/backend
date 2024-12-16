package com.bjcareer.search.application.port.out.persistence.ranking;

import java.util.List;

import org.springframework.data.util.Pair;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

public interface MarketRankingPort {
	void updateRankingNews(GPTStockNewsDomain news, Stock stock);
	List<GPTStockNewsDomain> getRankingNews();
	boolean isExistInCache(Stock stock);
	void removeRankingNews(Stock stock);
}
