package com.bjcareer.stockservice.timeDeal.performance.pk;


import com.bjcareer.stockservice.timeDeal.domain.coupon.CouponStatus;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.github.ksuid.KsuidGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class SeqCouponEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID couponNumber;
    private Double couponRate;

    //사용유무
    @Enumerated(value = EnumType.STRING)
    private CouponStatus status;

    //생성일자
    private LocalDate publishedDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "time_deal_event_id")
    private Event event;

    public SeqCouponEntity(Double couponRate, Event event) {
        this.couponRate = couponRate;
        this.status = CouponStatus.UNUSED;
        this.publishedDate = LocalDate.now();
        this.event = event;
    }


    @PrePersist
    private void createCouponNumber() {
        couponNumber = UUID.randomUUID();
    }
}
