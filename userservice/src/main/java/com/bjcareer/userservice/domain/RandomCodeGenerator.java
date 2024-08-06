package com.bjcareer.userservice.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class RandomCodeGenerator {
    public static Long generate() {
        Random random = new Random();
        return (long) (1000 + random.nextInt(9000));
    }
}
