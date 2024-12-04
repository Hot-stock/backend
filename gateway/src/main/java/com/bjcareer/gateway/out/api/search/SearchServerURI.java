package com.bjcareer.gateway.out.api.search;

public class SearchServerURI {
	public static final String FILTER_THEMA_SEARCH_RESULT = "/api/v0/search/thema";
	public static final String FILTER_STOCK_SEARCH_RESULT = "/api/v0//search/stock";
	public static final String FIND_RAISE_REASON = "/api/v0/search/raise-reason";

	public static final String NEXT_EVENT = "/api/v0/event";
	public static final String FILTER_NEXT_SCHEDULE_BY_STOCKNAME = "/api/v0/event/next-schedule";

	public static final String KEYWORD_COUNT = "/api/v0/keyword";
	public static final String ADD_STOCK = "/api/v0/stock";
	public static final String TOP_STOCK_NEWS = "/api/v0/ranking/stocks";
	public static final String OHLC = "/api/v0/stock/{code}/ohlc";
}
