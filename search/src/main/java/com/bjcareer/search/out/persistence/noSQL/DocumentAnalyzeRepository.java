package com.bjcareer.search.out.persistence.noSQL;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.application.port.out.persistence.stock.LoadStockRaiseReason;
import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaNewsCommand;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThema;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DocumentAnalyzeRepository {
	private final MongoCollection<Document> stockNewsCollection;
	private final MongoCollection<Document> themaNewsCollection;
	public static final String COLLECTION_NAME = "news";

	public DocumentAnalyzeRepository(MongoClient mongoClient) {
		stockNewsCollection = mongoClient.getDatabase(COLLECTION_NAME).getCollection(COLLECTION_NAME);
		themaNewsCollection = mongoClient.getDatabase(COLLECTION_NAME).getCollection("stock-thema-news");
	}

	public List<GPTNewsDomain> getUpcomingNews() {
		List<Document> documents = stockNewsCollection.find(Filters.gt("next", LocalDate.now(AppConfig.ZONE_ID)))
			.sort(Sorts.ascending("next")).into(new ArrayList<>());
		List<GPTNewsDomain> result = convertDocumentsToGPTNewsDomains(documents);
		return result;
	}

	public List<GPTNewsDomain> getUpcomingNewsByStockName(String stockName) {
		List<Document> documents = stockNewsCollection.find(
			Filters.and(
				Filters.gt("next", LocalDate.now(AppConfig.ZONE_ID)),
				Filters.eq("stockName", stockName)
			)
		).sort(Sorts.ascending("next")).into(new ArrayList<>());

		List<GPTNewsDomain> result = convertDocumentsToGPTNewsDomains(documents);

		return result;
	}

	public List<GPTNewsDomain> getRaiseReason(LoadStockRaiseReason command) {
		Bson filter = Filters.and(
			Filters.eq("stockName", command.getStockName()),
			Filters.eq("isRelated", true)
		);

		if (command.getDate() != null) {
			LocalDateTime startDate = changeISOFormat(command.getDate(), "T00:00:00.000Z");
			LocalDateTime endDate = changeISOFormat(command.getDate(), "T23:59:59.000Z");

			filter = Filters.and(
				filter,
				Filters.gte("news.pubDate", startDate.toInstant(ZoneOffset.UTC)),
				Filters.lte("news.pubDate", endDate.toInstant(ZoneOffset.UTC)));
		}

		List<Document> documents = stockNewsCollection.find(filter)
			.sort(Sorts.descending("news.pubDate"))
			.into(new ArrayList<>());

		return convertDocumentsToGPTNewsDomains(documents);
	}
	public List<GPTThema> getThemaNews(LoadThemaNewsCommand command) {
		Bson filter = Filters.and(
			Filters.eq("themaInfo.name", command.getThemaName()),
			Filters.eq("isRelated", true)
		);

		if (command.getDate() != null) {
			filter = Filters.and(
				filter,
				Filters.eq("news.pubDate", command.getDate())
			);
		}

		List<Document> documents = themaNewsCollection.find(filter)
			.sort(Sorts.descending("news.pubDate"))
			.into(new ArrayList<>());

		return convertDocumentsToGPTThemaDomains(documents, command.getThemaName());
	}

	private List<GPTThema> convertDocumentsToGPTThemaDomains(List<Document> documents, String themaName) {
		List<GPTThema> result = new ArrayList<>();

		for (Document document : documents) {
			GPTThema gptThema = changeDocumentToGPTThemaDomain(document, themaName);
			result.add(gptThema);
		}

		return result;
	}

	private GPTThema changeDocumentToGPTThemaDomain(Document document, String themaName) {
		String summary = document.getString("summary");
		String upComingDate = getUpComingDate(document);
		String upComingDateReason = document.getString("upcomingDateReasonFact");
		News news = changeDocumentToNewsDomain(document);

		return new GPTThema(themaName, summary, upComingDate, upComingDateReason, news);
	}

	private List<GPTNewsDomain> convertDocumentsToGPTNewsDomains(List<Document> documents) {
		List<GPTNewsDomain> result = new ArrayList<>();
		for (Document document : documents) {
			Boolean isRelated = document.getBoolean("isRelated");

			if (!isRelated) {
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

		String next = getDate(document);

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

	private String getDate(Document document) {
		if (document.getDate("next") != null) {
			return document.getDate("next").toInstant().atZone(AppConfig.ZONE_ID).toLocalDate().toString();
		}
		return "";
	}

	private String getUpComingDate(Document document) {
		if (document.getDate("upcomingDate") != null) {
			return document.getDate("upcomingDate").toInstant().atZone(AppConfig.ZONE_ID).toLocalDate().toString();
		}
		return "";
	}

	private LocalDateTime changeISOFormat(LocalDate date, String time) {
		String target = date.toString() + time;
		LocalDateTime dateTime = LocalDateTime.from(
			Instant.from(
				DateTimeFormatter.ISO_DATE_TIME.parse(target)
			).atZone(AppConfig.ZONE_ID));

		return dateTime;
	}

}
