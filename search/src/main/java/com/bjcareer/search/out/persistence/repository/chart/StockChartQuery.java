package com.bjcareer.search.out.persistence.repository.chart;

public class StockChartQuery {
	public static final String FIND_STOCK_BY_CODE = "SELECT s FROM Stock s WHERE s.code = :code";
	public static final String FIND_OHLC_ABOVE_THRESHOLD =
		"SELECT o FROM OHLC o" + " JOIN o.chart c" + " JOIN c.stock s"
			+ " WHERE s.code = :code"
			+ " AND o.percentageIncrease > :threshold";

	public static final String FIND_CHART_BY_DATE = "SELECT ohlc FROM OHLC ohlc "
		+ "JOIN ohlc.chart.stock stock "
		+ "WHERE stock.name = :stockName AND ohlc.date = :date";
}
