package com.bjcareer.search.retrieval.noSQL;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.bjcareer.search.repository.TrieRepository;
import com.bjcareer.search.retrieval.Trie;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MongoDbTrie implements Trie {
	private final TrieRepository repository;

	@Override
	public void insert(String str, Long searchCount){
		String query = "";
		ObjectId parendId = null;
		Document lastDocument = null;

		for (int i = 0; i < str.length(); i++) {
			query += str.charAt(i);

			Document parentNode = repository.findSingleByKeyword(MongoQueryKeywords.KEYWORD, query);

			if(parentNode != null){
				parendId = parentNode.getObjectId(MongoQueryKeywords.KEY);
			}else{
				lastDocument = changeNodeToDocument(query, parendId);
				parendId = repository.saveDocument(lastDocument);
			}
		}

		if (lastDocument == null) {
			return;
		}

		repository.updateKeyword(MongoQueryKeywords.SEARCH_COUNT, parendId, searchCount);
		repository.updateKeyword(MongoQueryKeywords.END_OF_WORD, parendId, true);

		updateParentToChild(lastDocument, parendId);
	}

	public List<String> search(String query){
		Document rootDocument = repository.findSingleByKeyword(MongoQueryKeywords.KEYWORD, query);
		return repository.getkeyworkList(rootDocument);
	}


	public void updateParentToChild(Document endOfDocument, ObjectId childId) {
		ObjectId parentId = endOfDocument.getObjectId(MongoQueryKeywords.PARENT_ID);

		while(parentId != null){
			repository.setChildIdToParentDocument(childId, parentId);
			parentId = repository.findByObjectId(parentId).getObjectId(MongoQueryKeywords.PARENT_ID);
		}
	}

	private Document changeNodeToDocument(String query, ObjectId parendId) {
		Document document = new Document();

		document.put(MongoQueryKeywords.KEYWORD, query);
		document.put(MongoQueryKeywords.PARENT_ID, parendId);
		document.put(MongoQueryKeywords.SEARCH_COUNT, 0);
		document.put(MongoQueryKeywords.END_OF_WORD, false);

		return document;
	}
}
