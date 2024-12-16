package com.bjcareer.search.domain.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ThemaInfo {
	@Id
	@GeneratedValue
	@Column(name = "THEMA_INFO_ID")
	private Long id;

	@Column(unique = true)
	private String name;
	private String href;

	@OneToMany(mappedBy = "themaInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@BatchSize(size = 10)
	private List<Thema> themas = new ArrayList<>();

	@JdbcTypeCode(SqlTypes.JSON)
	private ArrayNode news = JsonNodeFactory.instance.arrayNode();


	public ThemaInfo(String name, String href) {
		this.name = name;
		this.href = href;
	}

	public ThemaInfo(String name) {
		this(name, null);
	}

	public void addThemaNews(List<GPTThemaNewsDomain> themas) {
		ObjectMapper mapper = AppConfig.customObjectMapper();
		ArrayNode arrayNode = mapper.convertValue(themas, ArrayNode.class);

		this.news.addAll(arrayNode);
	}

	@Override
	public String toString() {
		return "ThemaInfo{" + "id=" + id + ", name='" + name + '\'' + ", href='" + href + '\'' + '}';
	}

	public List<GPTThemaNewsDomain> getNews() {
		return AppConfig.customObjectMapper().convertValue(news, new TypeReference<List<GPTThemaNewsDomain>>() {});
	}

}
