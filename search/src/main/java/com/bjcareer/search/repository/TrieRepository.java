package com.bjcareer.search.repository;

import static com.bjcareer.search.retrieval.MongoDbTrie.*;
import static com.mongodb.client.model.Filters.*;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.retrieval.MongoQueryKeywords;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import lombok.RequiredArgsConstructor;

@Repository
public class TrieRepository {
	private MongoCollection<Document> collection;
	public static final String TRIE_COLLECTION = "Trie";

	public TrieRepository(MongoDatabase database) {
		collection = database.getCollection(TRIE_COLLECTION);
	}

	public Document findSingleByKeyword(String keyword, Object query){
		return collection.find(eq(keyword, query)).first();
	}

	public MongoCursor<Document> findAllByKeyword(String keyword, Object query){
		return collection.find(eq(keyword, query)).cursor();
	}

	public Document findByObjectId(ObjectId objectId){
		return collection.find(eq(MongoQueryKeywords.KEY, objectId)).first();
	}

	public ObjectId saveDocument(Document document){
		collection.insertOne(document);
		return document.getObjectId(MongoQueryKeywords.KEY);
	}

	public void setChildIdToParentDocument(ObjectId childId, ObjectId parentId){
		collection.updateOne(
			eq(MongoQueryKeywords.KEY, parentId),
			Updates.addToSet("childs", childId)
		);
	}


	public void updateKeyword(String key, ObjectId id, Object object){
		collection.updateOne(eq(MongoQueryKeywords.KEY, id), Updates.set(key, object));
	}
}
