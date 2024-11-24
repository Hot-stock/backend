package com.bjcareer.GPTService.in.dtos;

import java.util.List;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

import lombok.Getter;

@Getter
public class RaiseStockReasonDTO {
	private NewsResponseDTO news;
	private String reason;

	public RaiseStockReasonDTO(NewsResponseDTO news, String reason) {
		this.news = news;
		this.reason = reason;
	}
}
