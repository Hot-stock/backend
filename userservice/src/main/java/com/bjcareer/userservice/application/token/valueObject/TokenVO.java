package com.bjcareer.userservice.application.token.valueObject;

import lombok.Data;

@Data
public class TokenVO {
    private final String telegramId;
    private final Long token;
}
