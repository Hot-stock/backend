package com.bjcareer.search.out.persistence.stock;

public class Query {
	public static final String FIND_STOCK_BY_CODE = "SELECT s FROM Stock s WHERE s.code = :code";
	public static final String FIND_STOCK_BY_NAME = "SELECT s FROM Stock s WHERE s.name = :name";
	public static final String FIND_ALL = "SELECT s FROM Stock s";

	public static final String FILTER_STOCK_BY_KEYWORD = "SELECT s FROM Stock s JOIN FETCH s.themas t JOIN FETCH t.themaInfo WHERE s.name LIKE :keyword";
}
