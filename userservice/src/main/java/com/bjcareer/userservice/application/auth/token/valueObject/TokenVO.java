package com.bjcareer.userservice.application.auth.token.valueObject;

import lombok.Data;

@Data
public class TokenVO {
    private final String telegramId;
    private final Long token;
}
