package com.bjcareer.userservice.service.vo;

import lombok.Data;

@Data
public class TokenVO {
    private final String telegramId;
    private final Long token;
}
