package com.bjcareer.payment.adapter.in.web;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

	@GetMapping("/success")
	public String suceessPayment(Model model){
		return "success";
	}

	@GetMapping("/fail")
	public String failPayment(Model model){
		return "fail";
	}
}
