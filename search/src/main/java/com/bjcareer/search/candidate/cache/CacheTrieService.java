package com.bjcareer.search.candidate.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;

import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.repository.cache.CacheRepository;
import com.bjcareer.search.repository.noSQL.DocumentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CacheTrieService implements Trie {
	private final CacheRepository repository;
	private final DocumentRepository documentRepository;

	@Override
	public void update(String keyword) {
		Document singleByKeyword = documentRepository.findSingleByKeyword(keyword);
		CacheNode node = new CacheNode(keyword, documentRepository.getkeyworkList(singleByKeyword));
		repository.saveKeyword(node);
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
