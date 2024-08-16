package com.bjcareer.stockservice.timeDeal.domain.event;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class Event {
    @Id @GeneratedValue
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

    public boolean incrementDeliveredCouponIfPossible(){
        log.debug("published coupon number is {}, deliveredCoupon number is {}", publishedCouponNum, deliveredCouponNum);
        if (deliveredCouponNum < publishedCouponNum) {
            this.deliveredCouponNum++;
            return true;
        }
        return false;
    }
}
