package com.bjcareer.search.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ranking")
public class Suggestion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String keyword;

	@Column(name = "search_count")
	private Long searchCount;

	public Suggestion(String keyword ,Long searchCount) {
		this.keyword = keyword;
		this.searchCount = searchCount;
	}

	public void updateSearchCount(){
		searchCount++;
	}
}
