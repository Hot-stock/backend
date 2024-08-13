package com.bjcareer.search.retrieval.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bjcareer.search.repository.cache.CacheRepository;
import com.bjcareer.search.retrieval.Trie;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CacheTrieService implements Trie {
	private final CacheRepository repository;

	@Override
	public void insert(String str, Long searchCount) {
		return;
	}

	@Override
	public List<String> search(String query) {
		Optional<CacheNode> node = repository.findByKeyword(query);

		if (node.isPresent()) {
			return node.get().getChild();
		}
		return new ArrayList<>();
	}
}
