package com.bjcareer.gateway.application.ports.out;

import com.bjcareer.gateway.application.ports.in.StockInfoCommand;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.domain.SearchResult;
import com.bjcareer.gateway.in.api.response.CandleResponseDTO;
import com.bjcareer.gateway.in.api.response.StockAdditionResponseDTO;
import com.bjcareer.gateway.out.api.search.response.NextEventNewsDTO;
import com.bjcareer.gateway.out.api.search.response.NextScheduleOfStockDTO;
import com.bjcareer.gateway.out.api.search.response.StockerFilterResultResponseDTO;
import com.bjcareer.gateway.out.api.search.response.TopNewsDTO;

public interface SearchServerPort {
	SearchResult searchResult(KeywordCommand command);
	ResponseDomain<StockerFilterResultResponseDTO> filterStockByQuery(KeywordCommand command);
	ResponseDomain<StockAdditionResponseDTO> addStockInfo(StockInfoCommand command);
	ResponseDomain<NextEventNewsDTO> getNextEventNewsFilterByStockName(String stockName);
	ResponseDomain<TopNewsDTO> findTopStockNews();
	ResponseDomain<CandleResponseDTO> getOHLC(String code, String period);
	ResponseDomain<NextEventNewsDTO> getNextEventNews();
}
