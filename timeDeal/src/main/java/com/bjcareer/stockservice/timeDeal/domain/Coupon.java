package com.bjcareer.stockservice.timeDeal.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class Coupon {
    @Id
    @GeneratedValue
    private Long id;

    //사용유무
    @Enumerated(value = EnumType.STRING)
    private CouponStatus status;

    //생성일자
    LocalDate publishedDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "time_deal_event_id")
    TimeDealEvent event;
}
