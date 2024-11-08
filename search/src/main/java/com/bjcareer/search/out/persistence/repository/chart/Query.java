package com.bjcareer.search.out.persistence.repository.chart;

public class Query {
	public static final String FIND_STOCK_BY_CODE = "SELECT s FROM Stock s WHERE s.code = :code";
	public static final String FIND_OHLC_ABOVE_THRESHOLD =
		"SELECT o FROM OHLC o" + " JOIN o.chart c"
			+ " WHERE c.stockCode = :code AND o.percentageIncrease >= :threshold";

	public static final String FIND_CHART_BY_DATE =
		"SELECT ohlc FROM OHLC ohlc "
			+ "JOIN ohlc.chart chart "
			+ "WHERE ohlc.date = :date";

	public static final String LOAD_STOCK_CHART = "SELECT sc FROM StockChart sc WHERE sc.stockCode = :code";
}
