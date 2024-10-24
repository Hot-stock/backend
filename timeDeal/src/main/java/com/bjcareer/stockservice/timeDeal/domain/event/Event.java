package com.bjcareer.stockservice.timeDeal.domain.event;

import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class Event {

    @Id
    @GeneratedValue
    @Column(name="event_id")
    private Long id;

    private int publishedCouponNum;
    private int deliveredCouponNum;
    private EventStatus status;
    private int discountPercentage;

    @Version
    private Long version;

    public Event(int publishedCouponNum, int discountPercentage) {
        this.publishedCouponNum = publishedCouponNum;
        this.deliveredCouponNum = 0;
        this.discountPercentage = discountPercentage;
        this.status = EventStatus.OPENED;
    }

    public void validateEventStatus() {
        if (isClosed()) {
            throw new InvalidEventException("Event status is CLOSED");
        }
    }

    public int deliverCoupons(int numberOfCoupons) {
        validateEventStatus();

        log.debug("Published coupon number: {}, Delivered coupon number: {}", publishedCouponNum, deliveredCouponNum);

        int newDeliveredCouponNum = deliveredCouponNum + numberOfCoupons;

        if (newDeliveredCouponNum >= publishedCouponNum) {
            closeEvent();
            int excessCoupons = newDeliveredCouponNum - publishedCouponNum;
            deliveredCouponNum = publishedCouponNum;
            return excessCoupons;
        }

        deliveredCouponNum = newDeliveredCouponNum;
        return 0;
    }

    private void closeEvent() {
        this.status = EventStatus.CLOSED;
    }

    private boolean isClosed() {
        return status == EventStatus.CLOSED;
    }
}
