package com.bjcareer.gateway.application.ports.out;

import java.util.List;

import com.bjcareer.gateway.application.ports.in.StockInfoCommand;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.domain.SearchResult;
import com.bjcareer.gateway.in.api.response.CandleResponseDTO;
import com.bjcareer.gateway.in.api.response.StockAdditionResponseDTO;
import com.bjcareer.gateway.in.api.response.TreeMapResponseDTO;
import com.bjcareer.gateway.out.api.search.response.GPTAnalayzeThemaNewsResponseDTO;
import com.bjcareer.gateway.out.api.search.response.NextEventNewsDTO;
import com.bjcareer.gateway.out.api.search.response.PageResponseDTO;
import com.bjcareer.gateway.out.api.search.response.RaiseReasonResponseDTO;
import com.bjcareer.gateway.out.api.search.response.RankStocksResponseDTO;
import com.bjcareer.gateway.out.api.search.response.StockerFilterResultResponseDTO;
import com.bjcareer.gateway.out.api.search.response.ThemaNamesResponseDTO;
import com.bjcareer.gateway.out.api.search.response.ThemaNewsOfStockResponseDTO;
import com.bjcareer.gateway.out.api.search.response.TopNewsDTO;

public interface SearchServerPort {
	SearchResult searchResult(KeywordCommand command);
	ResponseDomain<StockerFilterResultResponseDTO> filterStockByQuery(KeywordCommand command);
	ResponseDomain<StockAdditionResponseDTO> addStockInfo(StockInfoCommand command);
	ResponseDomain<NextEventNewsDTO> getNextEventNewsFilterByStockName(String stockName);
	ResponseDomain<TopNewsDTO> findTopStockNews();
	ResponseDomain<CandleResponseDTO> getOHLC(String code, String period);
	ResponseDomain<NextEventNewsDTO> getNextEventNews(int page, int size);

	ResponseDomain<RaiseReasonResponseDTO> findRaiseReasonOfStock(LoadRaiseReasonOfStock command);

	ResponseDomain<ThemaNewsOfStockResponseDTO> findThemaNews(LoadThemaNews command);

	ResponseDomain<RankStocksResponseDTO> getRankingStock();
	ResponseDomain<RankStocksResponseDTO> getSuggestionStock();


	ResponseDomain<List<TreeMapResponseDTO>> loadTreeMap();

	ResponseDomain<ThemaNamesResponseDTO> loadThemaNames();

	ResponseDomain<PageResponseDTO<GPTAnalayzeThemaNewsResponseDTO>> loadThemaNews(Integer id, int page, int size);


}
