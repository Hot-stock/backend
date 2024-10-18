package com.bjcareer.stockservice.timeDeal.in.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public static class GenerateCouponResponse {
        private String userId;
        private String message;

        public GenerateCouponResponse(String userId, int i) {
            this.userId = userId;
            this.message = userId + "는 " + i + "번째로 신청했습니다.";
        }
    }

}
