package com.bjcareer.payment.adapter.out.web.psp.exceptions;

import com.bjcareer.payment.adapter.out.web.psp.toss.exception.TossErrorCode;

public enum HotStockPspErrorCode {
	ACCOUNT_OWNER_CHECK_FAILED("계좌 소유주를 가져오는데 실패했습니다. 은행코드, 계좌번호를 확인해주세요."),
	ALREADY_COMPLETED_PAYMENT("이미 완료된 결제 입니다."),
	ALREADY_EXIST_SUBMALL("이미 존재하는 서브몰입니다."),
	BELOW_MINIMUM_AMOUNT("신용카드는 결제금액이 100원 이상, 계좌는 200원이상부터 결제가 가능합니다."),
	BELOW_ZERO_AMOUNT("금액은 0보다 커야 합니다."),
	DUPLICATED_ORDER_ID("이미 승인 및 취소가 진행된 중복된 주문번호 입니다. 다른 주문번호로 진행해주세요."),
	EXCEED_MAX_AMOUNT("거래금액 한도를 초과했습니다."),
	INVALID_ACCOUNT_NUMBER("계좌번호가 올바르지 않습니다."),
	INVALID_CARD("카드 입력정보가 올바르지 않습니다."),
	INVALID_CARD_EXPIRATION("카드 정보를 다시 확인해주세요.(유효기간)"),
	INVALID_CLIENT_KEY("잘못된 클라이언트 연동 정보 입니다."),
	INVALID_CURRENCY("잘못된 통화 값입니다."),
	INVALID_EMAIL("이메일 주소 형식에 맞지 않습니다."),
	INVALID_PASSWORD("결제 비밀번호가 일치하지 않습니다."),
	INVALID_PAYMENT_KEY("잘못된 결제 키 입니다."),
	NOT_FOUND("존재하지 않는 정보 입니다."),
	PAY_PROCESS_ABORTED("결제가 취소되었거나 진행되지 않았습니다."),
	PAY_PROCESS_CANCELED("결제가 사용자에 의해 취소되었습니다."),
	REFUND_REJECTED("환불이 거절됐습니다. 결제사에 문의 부탁드립니다.");

	private final String koreanMessage;

	HotStockPspErrorCode(String koreanMessage) {
		this.koreanMessage = koreanMessage;
	}


	public HotStockPspErrorCode getErrorCode(String msg) {
		return HotStockPspErrorCode.valueOf(msg);
	}

	public Boolean isSuccess(){
		return this == HotStockPspErrorCode.ALREADY_COMPLETED_PAYMENT;
	}

	public Boolean isFailure(){
		return switch (this) {
			case BELOW_MINIMUM_AMOUNT, BELOW_ZERO_AMOUNT, EXCEED_MAX_AMOUNT, INVALID_CARD, INVALID_ACCOUNT_NUMBER,
				 INVALID_PASSWORD, NOT_FOUND -> true;
			default -> false;
		};

	}

	public Boolean isUnknown(){
		return !isSuccess() && !isFailure();
	}

	public static HotStockPspErrorCode valueOf(TossErrorCode tossErrorCode){
		return HotStockPspErrorCode.valueOf(tossErrorCode.name());
	}
}
