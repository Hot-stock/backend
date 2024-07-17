package com.bjcareer.stockservice.search;


import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v0/search")
public class SearchController {

    // 사용자가 입력한 단어와 제한 수를 받아 예측된 단어 목록을 반환하는 메서드
    @PostMapping("/predictions")
    public void getPredictionWord(@RequestParam String word, @RequestParam int limit) {

    }
}