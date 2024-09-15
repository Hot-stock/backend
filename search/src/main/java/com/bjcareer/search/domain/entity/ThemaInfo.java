package com.bjcareer.search.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class ThemaInfo {
	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private String href;

	@OneToMany(mappedBy = "themaInfo")
	private List<Thema> themas = new ArrayList<>();

	public ThemaInfo(String name, String href) {
		this.name = name;
		this.href = href;
	}
}
