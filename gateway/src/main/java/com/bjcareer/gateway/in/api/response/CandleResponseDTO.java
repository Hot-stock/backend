package com.bjcareer.gateway.in.api.response;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class CandleResponseDTO {
	private Result result;

	@Data
	public static class Result {
		private String code;
		private List<Candle> candles;

		@Data
		public static class Candle {
			private String dt;
			private String startDate;
			private String endDate;
			private int open;
			private int high;
			private int low;
			private int close;
			private int volume;
			private int base;
		}
	}
}
