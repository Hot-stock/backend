package com.bjcareer.userservice.domain;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomCodeGenerator {
    public static Long generate() {
        Random random = new Random();
        return (long) (1000 + random.nextInt(9000));
    }
}
