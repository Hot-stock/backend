package com.bjcareer.userservice.controller.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

public class RegisterDTO {
    @Data
    @NoArgsConstructor
    public static class RegisterDtoRequest {
        private String userId;
        private String password;
        public String telegramId;
    }
}
