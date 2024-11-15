package com.bjcareer.search.out.persistence.repository.chart;

public class Query {
	public static final String FIND_OHLC_ABOVE_THRESHOLD =
		"SELECT sc FROM StockChart sc " +
			"JOIN FETCH sc.ohlcList o " +
			"WHERE sc.stockCode = :code " +
			"AND o.percentageIncrease >= :threshold " +
			"ORDER BY o.date DESC";
	;

	public static final String FIND_CHART_BY_DATE =
		"SELECT ohlc FROM OHLC ohlc "
			+ "JOIN ohlc.chart chart "
			+ "WHERE ohlc.date = :date";

	public static final String LOAD_STOCK_CHART = "SELECT sc FROM StockChart sc WHERE sc.stockCode = :code";

	public static final String LOAD_STOCK_CHART_BY_CODE = "SELECT sc FROM StockChart sc WHERE sc.stockCode = :code";
}
