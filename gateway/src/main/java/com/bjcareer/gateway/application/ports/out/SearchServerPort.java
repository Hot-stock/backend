package com.bjcareer.gateway.application.ports.out;

import com.bjcareer.gateway.domain.SearchCandidate;
import com.bjcareer.gateway.domain.SearchResult;

public interface SearchServerPort {
	SearchCandidate searchCandidate(KeywordCommand command);
	SearchResult searchResult(KeywordCommand command);
}
