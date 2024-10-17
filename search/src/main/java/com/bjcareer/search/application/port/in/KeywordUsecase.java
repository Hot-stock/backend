package com.bjcareer.search.application.port.in;

import java.util.List;

import com.bjcareer.search.domain.AbsoluteRankKeyword;

public interface KeywordUsecase {
	List<AbsoluteRankKeyword> getAbsoluteValueOfKeyword(String keyword);
}
