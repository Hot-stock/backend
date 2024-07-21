package com.bjcareer.stockservice.timeDeal;


import jakarta.persistence.GeneratedValue;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
public class TimeDealRepository {
    //회차
    
    @GeneratedValue
    private Long id;
}
