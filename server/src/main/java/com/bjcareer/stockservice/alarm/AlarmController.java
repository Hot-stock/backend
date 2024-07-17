package com.bjcareer.stockservice.alarm;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/alarm")
public class AlarmController {

    @GetMapping
    public void getAlarmStockList() {
        // 알람을 보내야 하는 리스트 순위를 조회하는 로직을 구현합니다.
        // 쿼리가 없다면 다른 알람을 보냅니다.
    }

    @PostMapping("/user/{userId}/{stockCode}")
    public void sendAlarmToSpecificUser(@PathVariable String userId, @PathVariable String stockCode) {
        // 특정 사용자에게 알람을 보내는 로직을 구현합니다.
    }

    @PostMapping("/group/{stockCode}/{groupId}")
    public void sendAlarmToSpecificGroup(@PathVariable String stockCode, @PathVariable String groupId) {
        // 특정 그룹에게 알람을 보내는 로직을 구현합니다.
    }
}
