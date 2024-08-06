package com.bjcareer.userservice.controller;


import static com.bjcareer.userservice.controller.dto.RegisterDTO.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api/v0/user")
public class UserController {

    @PostMapping
    public void Register(@RequestBody RegisterDtoRequest request) {

    }

}
