package com.bjcareer.stockservice.timeDeal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class TimeDealDTO {

    @Data
    @NoArgsConstructor
    public static class CreateTimeDealEventRequest {
        private int publishedCouponNumber;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateTimeDealEventResponse{
        Long enventId;
        int publishedCouponNumber;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenerateCouponResponse{
        String userId;
        UUID couponNumber;
    }
}
