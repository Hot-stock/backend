package com.bjcareer.search.out.persistence.thema;

public class Query {
	public static final String findThemaByName = "SELECT t FROM Thema t where t.themaInfo.name = :thema and t.stock.name = :stockName";
	public static final String findThemaInfoByName = "SELECT t FROM Thema t where t.themaInfo.name = :thema";
}
