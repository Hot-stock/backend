package com.bjcareer.stockservice.timeDeal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;

public class TimeDealDTO {

    @Data
    @NoArgsConstructor
    public static class CreateTimeDealEventRequest {
        private int publishedCouponNumber;
        private int discountRate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateTimeDealEventResponse{
        Long enventId;
        int publishedCouponNumber;
    }


    @Data
    public static class GenerateCouponResponse{
        String userId;
        UUID couponNumber;
        Long eventId;

        public GenerateCouponResponse(Coupon coupon) {
            this.userId = coupon.getUserPK();
            this.couponNumber = coupon.getCouponNumber();
            this.eventId = coupon.getEvent().getId();
        }
    }
}
