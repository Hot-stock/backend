package com.bjcareer.stockservice.timeDeal.controller;


import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.service.TimeDealService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/timedeal")
@Slf4j
public class TimeDealController {
    private final TimeDealService timeDealService;

    @GetMapping("start-time")
    public void getTimeDealStartTime(){
    }


    @PostMapping("/start")
    public CreateTimeDealEventResponse setTimeDealEvent(@RequestBody CreateTimeDealEventRequest request){
        TimeDealEvent timeDealEvent = timeDealService.createTimeDealEvent(request.getPublishedCouponNumber());
        CreateTimeDealEventResponse response = new CreateTimeDealEventResponse(200, timeDealEvent.getId(), timeDealEvent.getPublishedCouponNum());
        return response;
    }


    @PostMapping("tickets/{userId}/{eventId}")
    public GenerateCouponResponse generateTimeDealTicket(@PathVariable("userId") String userId, @PathVariable("eventId") Long eventId){
        log.debug(userId);
        try{
            Coupon coupon = timeDealService.generateCouponToUser(eventId, 20.0);
            GenerateCouponResponse response = new GenerateCouponResponse(200, coupon.getCouponNumber());
            return response;
        }catch (Exception e){
            log.error(e.getMessage());
            return new GenerateCouponResponse(400, UUID.randomUUID());
        }
    }


    @Getter
    @NoArgsConstructor
    public static class CreateTimeDealEventRequest {
        private int publishedCouponNumber;
    }

    @AllArgsConstructor
    @Getter
    static class CreateTimeDealEventResponse{
        int statusCode;
        Long enventId;
        int publishedCouponNumber;
    }

    @AllArgsConstructor
    @Getter
    static class GenerateCouponResponse{
        int statusCode;
        UUID couponNumber;
    }
}
