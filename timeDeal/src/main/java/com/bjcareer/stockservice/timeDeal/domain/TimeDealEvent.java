package com.bjcareer.stockservice.timeDeal.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TimeDealEvent {
    @Id @GeneratedValue
    @Column(name="time_deal_event_id")
    private Long id;

    //몇 개의 쿠폰이 발행했는지
    private int publishedCouponNum;

    //몇 개의 쿠푼이 사용자에게 전달됐는지
    private int deliveredCouponNum;

    @Version
    private Integer version;


    public TimeDealEvent(int publishedCouponNum) {
        this.publishedCouponNum = publishedCouponNum;
        this.deliveredCouponNum = 0;
    }

    public void updateDeliveredCouponNum(){
        this.deliveredCouponNum++;
    }
}
