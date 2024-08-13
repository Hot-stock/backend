package com.bjcareer.search.retrieval.noSQL;

public class MongoNode {
	String keyword = "";
	Long search_count = 0L;
	boolean endOfWord = false;
	Long parentId = null;

	public MongoNode(String keyword, Long search_count, boolean endOfWord, Long parentId) {
		this.keyword = keyword;
		this.search_count = search_count;
		this.endOfWord = endOfWord;
		this.parentId = parentId;
	}
}
