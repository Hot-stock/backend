package com.bjcareer.stockservice.ranking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v0/rank")
public class RankingController {

    @GetMapping("")
    //실시간 순위를 알려주는 로직이 필요함
    public void getRanking(){

    }
}
