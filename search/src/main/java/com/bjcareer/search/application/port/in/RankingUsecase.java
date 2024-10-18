package com.bjcareer.search.application.port.in;

import java.util.List;

public interface RankingUsecase {
	void updateKeyword(String keyword);
	List<String> getRankKeyword(Integer index);
}
