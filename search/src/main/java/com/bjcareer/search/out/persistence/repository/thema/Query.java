package com.bjcareer.search.out.persistence.repository.thema;

public class Query {
	public static final String findThemaByName = "SELECT t FROM Thema t where t.themaInfo.name = :thema";
	public static final String findThemaInfoByName = "SELECT t FROM Thema t where t.themaInfo.name = :thema";
}
