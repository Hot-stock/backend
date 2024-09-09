package com.bjcareer.payment.adapter.out.web.toss.response;

import lombok.Data;

@Data
public class TossPaymentExecutionResponse {
	private String mId;
	private String lastTransactionKey;
	private String paymentKey;
	private String orderId;
	private String orderName;
	private int taxExemptionAmount;
	private String status;
	private String requestedAt;  // ISO 8601 문자열로 되어 있으므로 String으로 받음
	private String approvedAt;   // ISO 8601 문자열로 되어 있으므로 String으로 받음
	private boolean useEscrow;
	private boolean cultureExpense;
	private Card card;
	private Object virtualAccount;
	private Object transfer;
	private Object mobilePhone;
	private Object giftCertificate;
	private Object cashReceipt;
	private Object cashReceipts;
	private Object discount;
	private Object cancels;
	private Object secret;
	private String type;
	private EasyPay easyPay;
	private int easyPayAmount;
	private int easyPayDiscountAmount;
	private String country;
	private Object failure;
	private boolean isPartialCancelable;
	private Receipt receipt;
	private Checkout checkout;
	private String currency;
	private int totalAmount;
	private int balanceAmount;
	private int suppliedAmount;
	private int vat;
	private int taxFreeAmount;
	private String method;
	private String version;

	// Getters and setters for each field
	// ...


	@Data
	public static class Card {
		private String issuerCode;
		private String acquirerCode;
		private String number;
		private int installmentPlanMonths;
		private boolean isInterestFree;
		private Object interestPayer;
		private String approveNo;
		private boolean useCardPoint;
		private String cardType;
		private String ownerType;
		private String acquireStatus;
		private String receiptUrl;
		private int amount;

		// Getters and setters for each field
		// ...
	}

	@Data
	public static class EasyPay {
		private String provider;
		private int amount;
		private int discountAmount;

		// Getters and setters for each field
		// ...
	}

	@Data
	public static class Receipt {
		private String url;

		// Getters and setters for each field
		// ...
	}

	@Data
	public static class Checkout {
		private String url;

		// Getters and setters for each field
		// ...
	}
}

