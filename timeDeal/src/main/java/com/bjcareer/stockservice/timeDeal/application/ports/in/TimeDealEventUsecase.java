package com.bjcareer.stockservice.timeDeal.application.ports.in;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;

public interface TimeDealEventUsecase {
	Event createEvent(int publishedCouponNum, int discountRate);
}
