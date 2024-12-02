package com.bjcareer.gateway.out.api.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.gateway.application.ports.in.StockInfoCommand;
import com.bjcareer.gateway.application.ports.out.KeywordCommand;
import com.bjcareer.gateway.application.ports.out.KeywordServerPort;
import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.AbsoluteRankKeyword;
import com.bjcareer.gateway.domain.ErrorDomain;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.domain.SearchCandidate;
import com.bjcareer.gateway.domain.SearchResult;
import com.bjcareer.gateway.in.api.response.CandleResponseDTO;
import com.bjcareer.gateway.in.api.response.StockAdditionResponseDTO;
import com.bjcareer.gateway.out.api.search.response.NextEventNewsDTO;
import com.bjcareer.gateway.out.api.search.response.NextScheduleOfStockDTO;
import com.bjcareer.gateway.out.api.search.response.TopNewsDTO;

@Component
public class SearchServerAPIAdapter implements KeywordServerPort, SearchServerPort {
	private final WebClient webClient;
	private final Logger log;

	public SearchServerAPIAdapter(@Qualifier("searchWebClient") WebClient webClient, Logger logger) {
		this.webClient = webClient;
		this.log = logger;
	}

	@Override
	public List<AbsoluteRankKeyword> searchCount(KeywordCommand command) {
		List<AbsoluteRankKeyword> result = webClient.get()
			.uri(SearchServerURI.KEYWORD_COUNT + "?q=" + command.getKeyword())
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<List<AbsoluteRankKeyword>>() {
			})
			.block();  // 동기적으로 결과 대기

		return result;  // 결과 반환
	}

	@Override
	public SearchCandidate searchCandidate(KeywordCommand command) {
		SearchCandidate result = webClient.get()
			.uri(SearchServerURI.SEARCH_CANDIDATE + "?q=" + command.getKeyword())
			.retrieve()
			.bodyToMono(SearchCandidate.class)
			.block();  // 동기적으로 결과 대기

		return result;  // 결과 반환
	}

	@Override
	public SearchResult searchResult(KeywordCommand command) {
		SearchResult result = webClient.get()
			.uri(SearchServerURI.SEARCH_RESULT + "?q=" + command.getKeyword())
			.retrieve()
			.bodyToMono(SearchResult.class)
			.block();  // 동기적으로 결과 대기

		log.info("Response of findNextScheduleOfStock: {}", result);

		return result;
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
	public ResponseDomain<NextScheduleOfStockDTO> findNextScheduleOfStock(String keyword) {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.FIND_NEXT_SCHEDULE + "?q=" + keyword)
			.exchange()
			.block();

		log.info("Response of {} {}", SearchServerURI.FIND_NEXT_SCHEDULE, res.statusCode());

		if (res.statusCode().is2xxSuccessful()) {
			NextScheduleOfStockDTO responseDTO = res.bodyToMono(NextScheduleOfStockDTO.class).block();
			return new ResponseDomain<>(res.statusCode(), responseDTO, null);
		} else {
			ErrorDomain errorDomain = res.bodyToMono(ErrorDomain.class).block();
			log.error("Error response of findNextScheduleOfStock: {}", errorDomain);
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
	public ResponseDomain<NextEventNewsDTO> getNextEventNews() {
		ClientResponse res = webClient.get()
			.uri(SearchServerURI.NEXT_EVENT)
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
}
