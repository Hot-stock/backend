package com.bjcareer.search.out.crawling.naver;

public class NaverFinanceCssQuery {

	public static final String GET_STOCK_LIST = "table.type_5 td a";
	public static final String GET_THEMA_LIST = "table.theme td.col_type1 a";
	public static final String GET_SHARED_STOCK = "th:contains(상장주식수) + td";
	public static final String GET_PRICE = "p.no_today span.blind";
	public static final String GET_MARKET_TYPE = ".wrap_company img";
	public static final String GET_STOCK_LINK = "/item/main.naver";
}
