package com.bjcareer.stockservice.timeDeal.controller;


import com.bjcareer.stockservice.timeDeal.controller.dto.TimeDealDTO.*;
import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.service.TimeDealService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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
    public ResponseEntity setTimeDealEvent(@RequestBody CreateTimeDealEventRequest request){
        TimeDealEvent timeDealEvent = timeDealService.createTimeDealEvent(request.getPublishedCouponNumber());
        CreateTimeDealEventResponse response = new CreateTimeDealEventResponse(timeDealEvent.getId(), timeDealEvent.getPublishedCouponNum());
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @PostMapping("tickets/{eventId}/{userId}")
    public ResponseEntity generateTimeDealTicket(@PathVariable("eventId") Long eventId, @PathVariable("userId") String userId){
        log.debug("User {} request coupon {}", userId, eventId);
        double DISCCOUT_RATE = 20.0;
        GenerateCouponResponse response = new GenerateCouponResponse();
        response.setUserId(userId);

        Optional<Coupon> coupon = timeDealService.generateCouponToUser(eventId, DISCCOUT_RATE);
        coupon.ifPresent(c -> response.setCouponNumber(c.getCouponNumber()));
        HttpStatus statusCode = coupon.isPresent() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity(response, statusCode);
    }
}
