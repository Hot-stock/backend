package com.bjcareer.gateway.out.api.search;

import org.springframework.web.bind.annotation.RequestMapping;

public class SearchServerURI {
	public static final String FILTER_THEMA_SEARCH_RESULT = "/api/v0/search/thema";
	public static final String FILTER_STOCK_SEARCH_RESULT = "/api/v0//search/stock";
	public static final String FIND_RAISE_REASON = "/api/v0/search/raise-reason";
	public static final String FIND_THEMA_NEWS_OF_STOCK = "/api/v0/search/news/thema";

	public static final String NEXT_EVENT = "/api/v0/event/schedule";
	public static final String FILTER_NEXT_SCHEDULE_BY_STOCKNAME = "/api/v0/event/next-schedule";

	public static final String TOP_STOCK_KEYWORD = "/api/v0/ranking/keywords";
	public static final String SUGGESTION_STOCK = "/api/v0/ranking/suggestion";

	public static final String ADD_STOCK = "/api/v0/stock";
	public static final String TOP_STOCK_NEWS = "/api/v0/ranking/stocks";
	public static final String OHLC = "/api/v0/stock/{code}/ohlc";
	public static final String TREE_MAP = "/api/v0/search/treemap";

	public static final String LOAD_THEMA_NAMES = "/api/v0/thema";
	public static final String LOAD_THEMA_NEWS = "/api/v0/thema/news";
}
