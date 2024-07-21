package com.bjcareer.stockservice.timeDeal;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/timedeal")
public class TimeDealController {

    @GetMapping("start-time")
    public void getTimeDealStartTime(){

    }

    @PostMapping("ticket")
    public void generateTimeDealTicket(){

    }
}
