package com.bjcareer.search.repository;

public class QueryConst {
	public static final String GET_SUGGESTION_KEYWORD = "SELECT r FROM Suggestion as r WHERE r.keyword LIKE :target ORDER BY r.searchCount DESC";
	public static final String UPDATE_SEARCH_COUNT = "SELECT r FROM Suggestion as r WHERE r.keyword = :target";
}

