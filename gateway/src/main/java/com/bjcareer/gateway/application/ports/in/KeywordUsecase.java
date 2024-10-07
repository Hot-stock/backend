package com.bjcareer.gateway.application.ports.in;

import java.util.List;

import com.bjcareer.gateway.domain.AbsoluteRankKeyword;

public interface KeywordUsecase {
	List<AbsoluteRankKeyword> getSearchCount(String keyword);
}
