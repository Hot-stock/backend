package com.bjcareer.payment.adapter.in.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bjcareer.payment.adapter.in.request.TossPaymentConfirmRequest;
import com.bjcareer.payment.application.port.in.PaymentConfirmCommand;
import com.bjcareer.payment.application.port.in.PaymentConfirmUsecase;
import com.bjcareer.payment.adapter.in.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/v0/payment")
@RequiredArgsConstructor
public class TossPaymentController {
	private final PaymentConfirmUsecase paymentConfirmUsecase;

	@PostMapping("/confirm")
	public Mono<ResponseEntity<ApiResponse<?>>> confirm(@RequestBody TossPaymentConfirmRequest request) {
		PaymentConfirmCommand paymentConfirmCommand = new PaymentConfirmCommand(request.getPaymentKey(),
			request.getOrderId(), request.getAmount());

		return paymentConfirmUsecase.confirm(paymentConfirmCommand)
			.map(it -> new ResponseEntity<>(
				new ApiResponse<>(HttpStatus.CREATED, "승인 성공", it),
				HttpStatus.CREATED
			));
	}
}
