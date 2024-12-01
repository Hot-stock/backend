package com.bjcareer.GPTService.out.api.toss;

import java.util.List;

import lombok.Data;

@Data
public class SoarStockResponseDTO {
	private Result result;

	@Data
	public static class Result {
		private String basedAt;
		private String type;
		private String duration;
		private List<Product> products;

		@Data
		public static class Product {
			private String productCode;
			private String name;
			private String logoImageUrl;
			private Price price;

			@Data
			public static class Price {
				private String priceType;
				private Double base;
				private Double close;
				private Double baseKrw;
				private Double closeKrw;
				private Long marketVolume;
				private Long marketAmount;
			}
		}
	}
}
