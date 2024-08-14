package com.bjcareer.search.repository.noSQL;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.retrieval.noSQL.DocumentQueryKeywords;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

@Repository
public class DocumentRepository {
	private Map<Integer, MongoCollection<Document>> collections;
	public static final String TRIE_COLLECTION_NAME = "Trie";
	private Map<String, Integer> shardingKey;

	public DocumentRepository(List<MongoDatabase> databases, Map<String, Integer> shardingKey) {
		collections = new HashMap<>();

		for (int i = 0; i < databases.size(); i++) {
			collections.put(i, databases.get(i).getCollection(TRIE_COLLECTION_NAME));
		}

		this.shardingKey = shardingKey;
	}


	public Document findSingleByKeyword(String keyword, String query){
		MongoCollection<Document> collection = getDocumentMongoCollection(query);
		return collection.find(eq(keyword, query)).first();
	}

	public MongoCursor<Document> findAllByKeyword(String keyword, Object query){
		MongoCollection<Document> collection = getDocumentMongoCollection(keyword);
		return collection.find(eq(keyword, query)).cursor();
	}

	public Document findByObjectId(String keyword, ObjectId objectId){
		MongoCollection<Document> collection = getDocumentMongoCollection(keyword);
		return collection.find(eq(DocumentQueryKeywords.KEY, objectId)).first();
	}

	public ObjectId saveDocument(String keyword, Document document){
		MongoCollection<Document> collection = getDocumentMongoCollection(keyword);
		collection.insertOne(document);
		return document.getObjectId(DocumentQueryKeywords.KEY);
	}

	public void setChildIdToParentDocument(String keyword, ObjectId childId, ObjectId parentId){
		MongoCollection<Document> collection = getDocumentMongoCollection(keyword);
		collection.updateOne(
			eq(DocumentQueryKeywords.KEY, parentId),
			Updates.addToSet("childs", childId)
		);
	}

	public void updateKeyword(String keyword, String key, ObjectId id, Object object){
		MongoCollection<Document> collection = getDocumentMongoCollection(keyword);
		collection.updateOne(eq(DocumentQueryKeywords.KEY, id), Updates.set(key, object));
	}

	public void updateSearchCount(String keyword){
		MongoCollection<Document> collection = getDocumentMongoCollection(keyword);
		collection.updateOne(eq(DocumentQueryKeywords.KEYWORD, keyword), Updates.inc(DocumentQueryKeywords.KEYWORD, 1));
	}

	public List<String> getkeyworkList(String keyword, Document rootDocument){
		List<String> result = new ArrayList<>();

		if (rootDocument == null) {
			return result;
		}

		List<ObjectId> childs = rootDocument.getList(DocumentQueryKeywords.CHILDS, ObjectId.class);

		if (childs != null) {
			childs.stream().forEach(c -> result.add(findByObjectId(keyword, c).get(DocumentQueryKeywords.KEYWORD, String.class)));
		}

		return result;
	}

	public Map<String, Integer> getKeywordCount(){
		Map<String, Integer> resultMap = new HashMap<>();
		// 집계 파이프라인
		List<Document> pipeline = Arrays.asList(
			new Document("$project", new Document("firstChar", new Document("$substr", Arrays.asList("$keyword", 0, 1)))),
			new Document("$group", new Document("_id", "$firstChar").append("count", new Document("$sum", 1))),
			new Document("$sort", new Document("count", -1))
		);

		// 집계 실행 및 결과 출력

		// 집계 실행 및 결과를 HashMap으로 저장
		Collection<MongoCollection<Document>> values = collections.values();

		for (MongoCollection<Document> collection : values) {
			collection.aggregate(pipeline).forEach(document -> {
				String key = document.getString("_id");
				Integer count = document.getInteger("count");
				resultMap.put(key, count);
			});
		}

		return resultMap;
	}

	private MongoCollection<Document> getDocumentMongoCollection(String keyword) {
		char c = keyword.charAt(0);
		System.out.println("c = " + c);
		System.out.println("shardingKey = " + shardingKey);
		Integer index = shardingKey.get(String.valueOf(c));
		System.out.println("index = " + index);
		MongoCollection<Document> collection = collections.get(index);
		System.out.println("collection = " + collection);
		return collection;
	}
}
