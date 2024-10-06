package com.bjcareer.gateway.application.ports.out;

import java.util.List;

import com.bjcareer.gateway.domain.AbsoluteRankKeyword;

public interface KeywordServerPort {
	List<AbsoluteRankKeyword> searchCount(KeywordCommand command);
}
