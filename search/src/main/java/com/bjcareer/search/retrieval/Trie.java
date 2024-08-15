package com.bjcareer.search.retrieval;

import java.util.List;

public interface Trie {
	public void insert(String str, Long searchCount);
	public List<String> search(String query);
}
