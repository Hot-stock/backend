package com.bjcareer.search.out.persistence.noSQL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DocumentAnalyzeRepository {
	private final MongoCollection<Document> collection;
	public static final String COLLECTION_NAME = "news";

	public DocumentAnalyzeRepository(MongoClient mongoClient) {
		collection = mongoClient.getDatabase("news").getCollection("news");
	}

	public List<GPTNewsDomain> getUpcomingNews() {
		List<GPTNewsDomain> result = new ArrayList<>();
		List<Document> documents = collection.find(Filters.gt("next", LocalDate.now(AppConfig.ZONE_ID)))
			.into(new ArrayList<>());

		for (Document document : documents) {
			Boolean isRelated = document.getBoolean("isRelated");

			if(!isRelated){
				continue;
			}


			GPTNewsDomain gptNewsDomain = changeDocumentToGPTNewsDomain(document);
			News news = changeDocumentToNewsDomain(document);

			gptNewsDomain.addNewsDomain(news);
			result.add(gptNewsDomain);
		}

		return result;
	}

	private GPTNewsDomain changeDocumentToGPTNewsDomain(Document document) {
		String stockName = document.getString("stockName");
		String reason = document.getString("reason");
		LocalDate next = document.getDate("next").toInstant().atZone(AppConfig.ZONE_ID).toLocalDate();
		String nextReason = document.getString("nextReasonFact");

		return new GPTNewsDomain(stockName, reason, new ArrayList<>(), next.toString(), nextReason);
	}

	private News changeDocumentToNewsDomain(Document document) {
		Document newsDocument = document.get("news", Document.class);

		String title = newsDocument.getString("title");
		String newsLink = newsDocument.getString("newsLink");
		String imgLink = newsDocument.get("imgLink", String.class);
		String description = newsDocument.getString("description");
		LocalDate pubDate = newsDocument.getDate("pubDate").toInstant().atZone(AppConfig.ZONE_ID).toLocalDate();
		String content = newsDocument.getString("content");

		return new News(title, newsLink, imgLink, description, pubDate, content);
	}
}
