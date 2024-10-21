package com.bjcareer.stockservice.timeDeal.in.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.stockservice.timeDeal.application.ports.in.AddParticipantCommand;
import com.bjcareer.stockservice.timeDeal.application.ports.in.CouponUsecase;
import com.bjcareer.stockservice.timeDeal.application.ports.in.CreateEventCommand;
import com.bjcareer.stockservice.timeDeal.application.ports.in.CreateEventUsecase;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.in.api.dto.TimeDealDTO.CreateTimeDealEventRequest;
import com.bjcareer.stockservice.timeDeal.in.api.dto.TimeDealDTO.CreateTimeDealEventResponse;
import com.bjcareer.stockservice.timeDeal.in.api.dto.TimeDealDTO.GenerateCouponResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/time-deal")
@Slf4j
public class TimeDealController {
    private final CouponUsecase couponUsecase;
    private final CreateEventUsecase usecase;

    @PostMapping("/open")
    public ResponseEntity<?> openTimeDealEvent(@RequestBody CreateTimeDealEventRequest request){
        validateOpenEventInputs(request);

        CreateEventCommand command = new CreateEventCommand(request.getPublishedCouponNumber(),
            request.getDiscountRate());
        Event event = usecase.createEvent(command);
        CreateTimeDealEventResponse response = new CreateTimeDealEventResponse(event.getId(),
            event.getPublishedCouponNum());

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("tickets/{eventId}")
    public ResponseEntity generateTimeDealTicket(@PathVariable("eventId") Long eventId,
        @CookieValue("SESSION_ID") String sessionId) {
        log.info("User {} request coupon {}", eventId, sessionId);
        AddParticipantCommand command = new AddParticipantCommand(sessionId, eventId);
        int turn = couponUsecase.addParticipation(command);
        GenerateCouponResponse response = new GenerateCouponResponse(turn);
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
