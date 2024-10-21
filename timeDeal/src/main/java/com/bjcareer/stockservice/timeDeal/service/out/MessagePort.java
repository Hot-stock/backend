package com.bjcareer.stockservice.timeDeal.service.out;

public interface MessagePort {
	void sendCouponMessage(String topic, CouponMessageCommand command);
}
