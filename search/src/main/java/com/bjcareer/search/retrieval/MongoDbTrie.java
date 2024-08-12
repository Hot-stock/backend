package com.bjcareer.search.retrieval;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.bjcareer.search.repository.TrieRepository;
import com.mongodb.client.MongoCursor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MongoDbTrie implements Trie{
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
		List<String> result = new ArrayList<>();
		MongoCursor<Document> rootDocument = repository.findAllByKeyword(MongoQueryKeywords.KEYWORD, query);

		if (rootDocument == null) {
			return result;
		}

		searchChild(rootDocument, result);

		return result;
	}


	public void updateParentToChild(Document endOfDocument, ObjectId childId) {
		ObjectId parentId = endOfDocument.getObjectId(MongoQueryKeywords.PARENT_ID);

		while(parentId != null){
			repository.setChildIdToParentDocument(childId, parentId);
			parentId = repository.findByObjectId(parentId).getObjectId(MongoQueryKeywords.PARENT_ID);
		}
	}

	private void searchChild(MongoCursor<Document> rootDocument, List<String> result) {
		Queue<ObjectId> queue = new LinkedList<>();
		queue.add(rootDocument.next().getObjectId(MongoQueryKeywords.KEY)); //부모아이디를 넣어줌

		while (!queue.isEmpty()) {
			ObjectId parentId = queue.poll();
			MongoCursor<Document> currentDocument = repository.findAllByKeyword(MongoQueryKeywords.PARENT_ID, parentId);

			if(currentDocument == null){
				continue;
			}

			addCurrentIdToQueueAndSaveEndOfword(result, currentDocument, queue);
		}
	}

	private void addCurrentIdToQueueAndSaveEndOfword(List<String> result, MongoCursor<Document> currentDocument, Queue<ObjectId> queue) {
		while(currentDocument.hasNext()){
			Document current = currentDocument.next();
			queue.add(current.getObjectId(MongoQueryKeywords.KEY));

			if (current.getBoolean(MongoQueryKeywords.END_OF_WORD, false)) {
				result.add(current.getString(MongoQueryKeywords.KEYWORD));
			}
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
