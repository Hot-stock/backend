package com.bjcareer.gateway.out.api.search;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.bjcareer.gateway.application.ports.in.StockInfoCommand;
import com.bjcareer.gateway.application.ports.out.KeywordCommand;
import com.bjcareer.gateway.application.ports.out.LoadRaiseReasonOfStock;
import com.bjcareer.gateway.application.ports.out.LoadThemaNews;
import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.domain.ErrorDomain;
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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SearchServerAPIAdapter implements SearchServerPort {
	private final WebClient webClient;

	public SearchServerAPIAdapter(@Qualifier("searchWebClient") WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public SearchResult searchResult(KeywordCommand command) {
		SearchResult result = webClient.get()
			.uri(SearchServerURI.FILTER_THEMA_SEARCH_RESULT + "?q=" + command.getKeyword())
			.retrieve()
			.bodyToMono(SearchResult.class)
			.block();  // 동기적으로 결과 대기

		log.info("Response of findNextScheduleOfStock: {}", result);

		return result;
	}

	@Override
	public ResponseDomain<StockerFilterResultResponseDTO> filterStockByQuery(KeywordCommand command) {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.FILTER_STOCK_SEARCH_RESULT + "?q=" + command.getKeyword())
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.FILTER_STOCK_SEARCH_RESULT, res.statusCode());
		if (res.statusCode().is2xxSuccessful()) {
			StockerFilterResultResponseDTO responseDTO = res.bodyToMono(StockerFilterResultResponseDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of addStockInfo: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<RaiseReasonResponseDTO> findRaiseReasonOfStock(LoadRaiseReasonOfStock command) {
		String uri = UriComponentsBuilder.fromUriString(SearchServerURI.FIND_RAISE_REASON)
			.queryParam("q", command.getStockCode()) // stockName은 항상 추가
			.queryParamIfPresent("date", Optional.ofNullable(command.getDate())) // date는 null이 아니면 추가
			.toUriString();

		ClientResponse res = webClient.get()
			.uri(uri)
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.FIND_RAISE_REASON, res.statusCode());
		if (res.statusCode().is2xxSuccessful()) {
			RaiseReasonResponseDTO responseDTO = res.bodyToMono(RaiseReasonResponseDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of addStockInfo: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<ThemaNewsOfStockResponseDTO> findThemaNews(LoadThemaNews command) {
		String uri = UriComponentsBuilder.fromUriString(SearchServerURI.FIND_THEMA_NEWS_OF_STOCK)
			.queryParam("q", command.getCode())
			.queryParam("theme", command.getName())
			.queryParamIfPresent("date", Optional.ofNullable(command.getDate())) // date는 null이 아니면 추가
			.toUriString();

		ClientResponse res = webClient.get()
			.uri(uri)
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.FIND_THEMA_NEWS_OF_STOCK, res.statusCode());
		if (res.statusCode().is2xxSuccessful()) {
			ThemaNewsOfStockResponseDTO responseDTO = res.bodyToMono(ThemaNewsOfStockResponseDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of addStockInfo: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<StockAdditionResponseDTO> addStockInfo(StockInfoCommand command) {
		ClientResponse res = webClient.post()
			.uri(SearchServerURI.ADD_STOCK)
			.bodyValue(command)
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.ADD_STOCK, res.statusCode());
		if (res.statusCode().is2xxSuccessful()) {
			StockAdditionResponseDTO responseDTO = res.bodyToMono(StockAdditionResponseDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of addStockInfo: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<TopNewsDTO> findTopStockNews() {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.TOP_STOCK_NEWS)
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.TOP_STOCK_NEWS, res.statusCode());

		if (res.statusCode().is2xxSuccessful()) {
			TopNewsDTO responseDTO = res.bodyToMono(TopNewsDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of findNextScheduleOfStock: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<CandleResponseDTO> getOHLC(String code, String period) {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.OHLC.replace("{code}", code) + "?period=" + period)
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.OHLC, res.statusCode());

		if (res.statusCode().is2xxSuccessful()) {
			CandleResponseDTO responseDTO = res.bodyToMono(CandleResponseDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of getOHLC: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<NextEventNewsDTO> getNextEventNews(int page, int size) {
		ClientResponse res = webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path(SearchServerURI.NEXT_EVENT)
				.queryParam("page", page)
				.queryParam("size", size)
				.build())
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.NEXT_EVENT, res.statusCode());

		if (res.statusCode().is2xxSuccessful()) {
			NextEventNewsDTO responseDTO = res.bodyToMono(NextEventNewsDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of getOHLC: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<NextEventNewsDTO> getNextEventNewsFilterByStockName(String stockName) {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.FILTER_NEXT_SCHEDULE_BY_STOCKNAME + "?q=" + stockName)
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.FILTER_NEXT_SCHEDULE_BY_STOCKNAME, res.statusCode());
		if (res.statusCode().is2xxSuccessful()) {
			NextEventNewsDTO responseDTO = res.bodyToMono(NextEventNewsDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of findNextScheduleOfStock: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<RankStocksResponseDTO> getRankingStock() {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.TOP_STOCK_KEYWORD)
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.TOP_STOCK_KEYWORD, res.statusCode());

		if (res.statusCode().is2xxSuccessful()) {
			RankStocksResponseDTO responseDTO = res.bodyToMono(RankStocksResponseDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of findNextScheduleOfStock: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<RankStocksResponseDTO> getSuggestionStock() {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.SUGGESTION_STOCK)
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.SUGGESTION_STOCK, res.statusCode());

		if (res.statusCode().is2xxSuccessful()) {
			RankStocksResponseDTO responseDTO = res.bodyToMono(RankStocksResponseDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of findNextScheduleOfStock: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<List<TreeMapResponseDTO>> loadTreeMap() {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.TREE_MAP)
			.exchange()
			.block();

		if (res.statusCode().is2xxSuccessful()) {
			List<TreeMapResponseDTO> responseDTO = res.bodyToFlux(TreeMapResponseDTO.class).collectList().block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of findNextScheduleOfStock: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<ThemaNamesResponseDTO> loadThemaNames() {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.LOAD_THEMA_NAMES)
			.exchange()
			.block();

		if (res.statusCode().is2xxSuccessful()) {
			ThemaNamesResponseDTO responseDTO = res.bodyToMono(ThemaNamesResponseDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of findNextScheduleOfStock: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}

	@Override
	public ResponseDomain<PageResponseDTO<GPTAnalayzeThemaNewsResponseDTO>> loadThemaNews(Integer id, int page,
		int size) {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.LOAD_THEMA_NEWS + "?q=" + id + "&page=" + page + "&size=" + size)
			.exchange()
			.block();

		if (res.statusCode().is2xxSuccessful()) {
			PageResponseDTO<GPTAnalayzeThemaNewsResponseDTO> responseDTO = res.bodyToMono(
				new ParameterizedTypeReference<PageResponseDTO<GPTAnalayzeThemaNewsResponseDTO>>() {
				}
			).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of findNextScheduleOfStock: {}", errorDomain);
			return new ResponseDomain<>(res.statusCode(), null, errorDomain);
		}
	}
}
