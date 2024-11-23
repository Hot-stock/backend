package com.bjcareer.GPTService.in.dtos;

import java.util.List;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

import lombok.Getter;

@Getter
public class RaiseStockReasonDTO {
	private NewsResponseDTO news;
	private List<ThemaResponseDTO> thema;
	private String reason;

	public RaiseStockReasonDTO(NewsResponseDTO news, List<GPTNewsDomain.GPTThema> themas, String reason) {
		this.news = news;
		thema = themas.stream().map(ThemaResponseDTO::new).toList(); // 로컬 변수로 처리됨
		this.reason = reason;
	}
}
