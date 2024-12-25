package com.bjcareer.search.in.api.controller.dto;

import java.util.List;

import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadThemaNameResDTO {
	private final Integer total;
	private final List<Content> contents;

	public LoadThemaNameResDTO(List<ThemaInfo> themaInfos) {
		this.total = themaInfos.size();
		this.contents = themaInfos.stream().map((t -> new Content(t.getId(), t.getName()))).toList();
	}

	@Data
	private static class Content {
		private final Long id;
		private final String thema;
	}
}
