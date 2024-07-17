package com.bjcareer.stockservice.payment;


import com.oracle.svm.core.annotate.Delete;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v0/payment")
public class PaymentController {

    @PostMapping("/payment")
    public void doPayment(){
        //먹등성이 가능하도록 설계해야 함

    }

    @PostMapping("/payment/{paymentId}/cancle")
    public void deletePayment(){

    }

    //상태 조회
    @GetMapping("/payment/{paymentId}")
    public void getpayment(){
    }


}
