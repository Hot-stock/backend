package com.bjcareer.stockservice.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/users")
public class UserController {

    @PostMapping("/register")
    public void register(){

    }

    @DeleteMapping("/{userId}")
    public void deleteUser(){

    }
}
