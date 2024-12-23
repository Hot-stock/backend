package com.bjcareer.search.event.messages;

import com.bjcareer.search.domain.entity.Stock;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateStockLogoMessage {
	private final Stock stock;
}
