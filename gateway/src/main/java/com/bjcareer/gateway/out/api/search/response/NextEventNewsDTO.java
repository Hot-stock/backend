package com.bjcareer.gateway.out.api.search.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class NextEventNewsDTO {
	private Integer total;
	private List<EventDTO> items;

	@Getter
	private static class EventDTO {
		private String stockName;
		private String nextEventReason;
		private String imgLink;
		private String link;
		private LocalDate nextEventDate;
	}
}
