package com.bjcareer.userservice.application.auth.token.valueObject;

import lombok.Data;

@Data
public class TokenVO {
    private final String email;
    private final Long token;
}
