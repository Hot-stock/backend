package com.bjcareer.stockservice.timeDeal.controller;


import com.bjcareer.stockservice.timeDeal.controller.dto.TimeDealDTO.*;
import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.service.TimeDealService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/time-deal")
@Slf4j
public class TimeDealController {
    private final TimeDealService timeDealService;

    @GetMapping("start-time")
    public void getTimeDealStartTime(){
    }


    @PostMapping("/open")
    public ResponseEntity<?> openTimeDealEvent(@RequestBody CreateTimeDealEventRequest request){
        validateOpenEventInputs(request);

        Event timeDealEvent = timeDealService.createEvent(request.getPublishedCouponNumber(), request.getDiscountRate());
        CreateTimeDealEventResponse response = new CreateTimeDealEventResponse(timeDealEvent.getId(), timeDealEvent.getPublishedCouponNum());

        return new ResponseEntity(response, HttpStatus.OK);
    }


    @PostMapping("tickets/{eventId}/{userId}")
    public ResponseEntity generateTimeDealTicket(@PathVariable("eventId") Long eventId, @PathVariable("userId") String userId){
        log.debug("User {} request coupon {}", userId, eventId);
        Coupon coupon = timeDealService.generateCouponToUser(eventId, userId);
        GenerateCouponResponse response = new GenerateCouponResponse(coupon);

        return new ResponseEntity(response, HttpStatus.OK);
    }


    private void validateOpenEventInputs(CreateTimeDealEventRequest request) {
        if (request.getPublishedCouponNumber() <= 0) {
            throw new IllegalArgumentException("Published coupon number must be greater than zero");
        }
        if (request.getDiscountRate() < 0 || request.getDiscountRate() > 100) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 100");
        }
    }
}
