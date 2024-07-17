package com.bjcareer.stockservice.alarm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v0/alarm")
public class AlarmController {

    @GetMapping("")
    //알람을 보내야 하는 리스트 순위
    public void alarmStock(){
        //쿼리가 없다면 다른 알람
    }

    //특정인에게만 보내주는 알람
    @PostMapping("/user/{userId}/{stockCode}")
    public void sendAlarmToUser(){
    }

    //특정 그룹에게만 보내주는 알람
    @PostMapping("/group/{stockCode}/{groupId}")
    public void sendAlarmToGroup(){
    }

}
