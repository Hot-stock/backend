package com.bjcareer.search.out.persistence.noSQL;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.domain.gpt.insight.GPTInsight;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DocumentAnalyzeInsightRepository {
	private final MongoCollection<Document> insightCollection;
	public static final String COLLECTION_NAME = "news";
	private static final String DATABASE_NAME = "insight";

	public DocumentAnalyzeInsightRepository(MongoClient mongoClient) {
		insightCollection = mongoClient.getDatabase(COLLECTION_NAME).getCollection(DATABASE_NAME);
	}

	public Optional<GPTInsight> getInsightOfStockByLatest(String stockName) {
		Bson filter = Filters.and(
			Filters.eq("stockName", stockName),
			Filters.eq(("isFound"), true));

		FindIterable<Document> documents = insightCollection.find(filter)
			.sort(Sorts.descending("createdDate"))
			.limit(1);

		if (!documents.cursor().hasNext()) {
			return Optional.empty();
		}

		return Optional.of(changeDocumentToGPTInsightDomain(documents.first(), stockName));
	}

	private GPTInsight changeDocumentToGPTInsightDomain(Document document, String themaName) {
		Boolean isFound = document.getBoolean("isFound");
		String stockName = document.getString("stockName");
		String reason = document.getString("reason");
		String reasonDetail = document.getString("reasonDetail");
		Integer score = document.getInteger("score");
		LocalDate createAt = document.getDate("createdDate").toInstant().atOffset(ZoneOffset.UTC).toLocalDate();

		return new GPTInsight(isFound, stockName, reason, reasonDetail, score, createAt);
	}
}
