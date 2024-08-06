package com.bjcareer.userservice.controller.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

public class RegisterDto {
    @Data
    public static class RegisterDtoRequest {
        private final String userId;
        private final String password;
        public final String telegramId;
    }

    @Data
    public static class MobileAuthentication {
        private final Long token;
        public final String telegramId;
    }

    @Data
    public static class MobileAuthenticationVerifyRequest {
        private final Long token;
        public final String telegramId;
    }

    @Data
    public static class MobileAuthenticationVerifyResponse {
        private final boolean result;
    }


    @Data
    public static class RegisterResponse {
        private final String id;
    }
}
