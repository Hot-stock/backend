package com.bjcareer.stockservice.timeDeal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LogController {

    @GetMapping("/log")
    public String log(){
        log.debug("log");
        log.info("log");
        log.warn("log");
        log.error("log");
        return "log";
    }
}
