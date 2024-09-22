package com.bjcareer.search.candidate.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;

import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.repository.cache.CacheRepository;
import com.bjcareer.search.repository.noSQL.DocumentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CacheTrieService implements Trie {
	private final CacheRepository cacheRepository;
	private final DocumentRepository documentRepository;

	@Override
	public void update(String keyword) {
		Document singleByKeyword = documentRepository.findSingleByKeyword(keyword);

		if (singleByKeyword == null) {
			log.info("Document not found for keyword: {}", keyword);
			return;
		}

		CacheNode node = new CacheNode(keyword, documentRepository.getkeyworkList(singleByKeyword));
		cacheRepository.saveKeyword(node);
	}

	@Override
	public List<String> search(String query) {
		Optional<CacheNode> node = cacheRepository.findByKeyword(query);

		if (node.isPresent()) {
			return node.get().getChild();
		}

		return new ArrayList<>();
	}
}
