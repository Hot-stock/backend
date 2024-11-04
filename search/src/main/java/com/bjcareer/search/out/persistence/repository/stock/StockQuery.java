package com.bjcareer.search.out.persistence.repository.stock;

public class StockQuery {
	public static final String FIND_STOCK_BY_CODE = "SELECT s FROM Stock s WHERE s.code = :code";
	public static final String FIND_ALL = "SELECT s FROM Stock s";
}
