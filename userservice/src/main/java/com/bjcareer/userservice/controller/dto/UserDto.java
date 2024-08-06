package com.bjcareer.userservice.controller.dto;

import lombok.Data;

public class UserDto {

    @Data
    public static class LoginRequest {
        private final String id;
        private final String password;
    }

    @Data
    public static class LoginResponse {
        private final String accessToken;
    }
}
