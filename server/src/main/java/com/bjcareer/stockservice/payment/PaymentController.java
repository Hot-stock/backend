package com.bjcareer.stockservice.payment;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/payment")
public class PaymentController {

    @PostMapping
    public void doPayment() {
        // 결제 처리 로직을 여기에 구현합니다.
    }

    @PostMapping("/{paymentId}/cancel")
    public void cancelPayment(@PathVariable String paymentId) {
        // 결제 취소 로직을 여기에 구현합니다.
    }

    @GetMapping("/{paymentId}")
    public void getDetailInfoPayment(@PathVariable String paymentId) {
        // 결제 상태 조회 로직을 여기에 구현합니다.
    }
}

