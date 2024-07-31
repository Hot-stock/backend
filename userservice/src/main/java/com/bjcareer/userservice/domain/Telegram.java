package com.bjcareer.userservice.domain;


import org.springframework.stereotype.Component;

@Component
public class Telegram {
    public boolean sendCode(String userId, Long code){
        if (userId == null){
            return false;
        }
        return true;
    }
}
