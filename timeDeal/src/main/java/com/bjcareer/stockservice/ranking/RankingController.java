package com.bjcareer.stockservice.ranking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/ranking")
public class RankingController {

    @GetMapping
    public void getRanking(@RequestParam int limit) {
    }
}