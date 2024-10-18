package com.bjcareer.stockservice.timeDeal.in.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.stockservice.timeDeal.application.ports.in.TimeDealEventUsecase;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.in.api.dto.TimeDealDTO.CreateTimeDealEventRequest;
import com.bjcareer.stockservice.timeDeal.in.api.dto.TimeDealDTO.CreateTimeDealEventResponse;
import com.bjcareer.stockservice.timeDeal.in.api.dto.TimeDealDTO.GenerateCouponResponse;
import com.bjcareer.stockservice.timeDeal.service.TimeDealService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/time-deal")
@Slf4j
public class TimeDealController {
    private final TimeDealService timeDealService;
    private final TimeDealEventUsecase usecase;

    @PostMapping("/open")
    public ResponseEntity<?> openTimeDealEvent(@RequestBody CreateTimeDealEventRequest request){
        validateOpenEventInputs(request);

        Event event = usecase.createEvent(request.getPublishedCouponNumber(), request.getDiscountRate());
        CreateTimeDealEventResponse response = new CreateTimeDealEventResponse(event.getId(),
            event.getPublishedCouponNum());

        return new ResponseEntity(response, HttpStatus.OK);
    }


    @PostMapping("tickets/{eventId}/{userId}")
    public ResponseEntity generateTimeDealTicket(@PathVariable("eventId") Long eventId, @PathVariable("userId") String userId){
        log.debug("User {} request coupon {}", userId, eventId);
        int turn = timeDealService.addParticipation(eventId, userId);
        GenerateCouponResponse response = new GenerateCouponResponse(userId, turn);

        return ResponseEntity.ok(response);
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
