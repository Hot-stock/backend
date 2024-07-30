package com.bjcareer.stockservice.timeDeal.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class TimeDealEvent {
    @Id @GeneratedValue
    @Column(name="time_deal_event_id")
    private Long id;

    //몇 개의 쿠폰이 발행했는지
    private int publishedCouponNum;

    //몇 개의 쿠푼이 사용자에게 전달됐는지
    private int deliveredCouponNum;


    public TimeDealEvent(int publishedCouponNum) {
        this.publishedCouponNum = publishedCouponNum;
        this.deliveredCouponNum = 0;
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
