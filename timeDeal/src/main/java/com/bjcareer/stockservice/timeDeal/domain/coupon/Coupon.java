package com.bjcareer.stockservice.timeDeal.domain.coupon;

import java.time.LocalDate;
import java.util.UUID;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"event_id", "client_pk"})
})
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true, nullable = false)
    private UUID couponNumber;

    @Enumerated(value = EnumType.STRING)
    private CouponStatus status;

    private LocalDate publishedDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "client_pk")
    private String userPK;


    public Coupon(Event event, String userPK) {
        this.status = CouponStatus.UNUSED;
        this.publishedDate = LocalDate.now();
        this.event = event;
        this.couponNumber = UUID.randomUUID();
        this.userPK = userPK;
    }
}
