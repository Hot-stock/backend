package com.bjcareer.search.application.information;

import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.bjcareer.search.application.port.out.persistence.ranking.MarketRankingPort;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotTopicService {
	private final MarketRankingPort marketRankingPort;

	public List<Pair<String, GPTNewsDomain>> getTrendingStory() {
		return marketRankingPort.getRankingNews();
	}
}
