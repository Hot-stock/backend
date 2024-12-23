package com.bjcareer.search.out.api.toss.dtos;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class TossStockInfoDTO {
	private Result result;

	@Getter
	public static class Result {
		private String code;
		private String guid;
		private String symbol;
		private String isinCode;
		private String status;
		private String name;
		private String englishName;
		private String detailName;
		private Market market;
		private Group group;
		private String companyCode;
		private String companyName;
		private String logoImageUrl;
		private String currency;
		private boolean tradingSuspended;
		private boolean commonShare;
		private boolean spac;
		private LocalDate spacMergerDate;
		private double leverageFactor;
		private boolean clearance;
		private String riskLevel;
		private String purchasePrerequisite;
		private long sharesOutstanding;
		private LocalDate prevListDate;
		private LocalDate listDate;
		private LocalDate delistDate;
		private Double offeringPrice;
		private String warrantsCode;
		private String warrantsGroupCode;
		private String etfTaxCode;
		private boolean daytimePriceSupported;
		private boolean optionSupported;
		private boolean optionPennyPilotPriceSupported;
		private String optionInstrument;
		private boolean derivativeEtp;
		private boolean poolingStock;
		private boolean derivativeEtf;

		@Getter
		public static class Market {
			private String code;
			private String displayName;
		}

		@Getter
		public static class Group {
			private String code;
			private String displayName;
		}
	}
}
