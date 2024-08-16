package com.bjcareer.stockservice.timeDeal.domain.coupon;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.domain.user.User;
import com.bjcareer.stockservice.timeDeal.domain.user.UserVO;
import com.github.ksuid.KsuidGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {
    @Id
    String id = KsuidGenerator.generate();

    @Column(unique = true, nullable = false)
    private UUID couponNumber;

    @Enumerated(value = EnumType.STRING)
    private CouponStatus status;

    private LocalDate publishedDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private UserVO userVO;

    public Coupon(Event event, UserVO user) {
        this.status = CouponStatus.UNUSED;
        this.publishedDate = LocalDate.now();
        this.event = event;
        this.couponNumber = UUID.randomUUID();
        this.userVO = user;
    }

}
