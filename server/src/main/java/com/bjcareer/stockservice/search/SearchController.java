package com.bjcareer.stockservice.search;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v0/search")
public class SearchController {

    //사용자가 입력한 단어가 넘어옴
    //쿼리파람있음
    @PostMapping("/predictions")
    public void getPredictionWord(@RequestParam String word, @RequestParam int limit){
    }
}
