package com.bjcareer.gateway.application.search;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bjcareer.gateway.application.ports.in.KeywordUsecase;
import com.bjcareer.gateway.application.ports.out.KeywordCommand;
import com.bjcareer.gateway.application.ports.out.KeywordServerPort;
import com.bjcareer.gateway.domain.AbsoluteRankKeyword;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeywordService implements KeywordUsecase {
	private final KeywordServerPort keywordServerPort;

	@Override
	public List<AbsoluteRankKeyword> getSearchCount(String keyword) {
		KeywordCommand command = new KeywordCommand(keyword);
		return keywordServerPort.searchCount(command);
	}
}
