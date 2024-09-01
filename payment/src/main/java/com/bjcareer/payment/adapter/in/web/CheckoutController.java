package com.bjcareer.payment.adapter.in.web;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bjcareer.payment.application.domain.CheckoutResult;
import com.bjcareer.payment.application.port.in.CheckoutCommand;
import com.bjcareer.payment.application.port.in.CheckoutUsecase;
import com.bjcareer.payment.adapter.in.request.CheckoutRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
public class CheckoutController {
	private final CheckoutUsecase checkoutUsecase;

	@GetMapping("")
	public String createCheckout(Model model){
		Random random = new Random();
		int cartId = random.nextInt(1000000);  // 0부터 9,999,999까지의 숫자 중 하나를 반환

		model.addAttribute("cartId", cartId);
		model.addAttribute("buyerPk", UUID.randomUUID().toString());
		model.addAttribute("productIds", new int[]{1});
		model.addAttribute("couponIds", new int[]{2});
		return "index";
	}

	@PostMapping("/checkout")
	public Mono<String> checkout(@RequestBody CheckoutRequest checkoutRequest, RedirectAttributes redirectAttributes){
		log.debug("checkoutRequest = " + checkoutRequest);
		CheckoutCommand checkoutCommand = new CheckoutCommand(checkoutRequest.getCartId(), checkoutRequest.getBuyerPk(),
			checkoutRequest.getProductIds(), checkoutRequest.getCouponIds());

		Mono<CheckoutResult> checkout = checkoutUsecase.checkout(checkoutCommand);
		return checkout.map(
			it -> {
				System.out.println("checkout = " + it);
				redirectAttributes.addFlashAttribute("checkoutResult", it);
				return "redirect:/payment/redir-checkout";
			}
		);
	}

	@GetMapping("/redir-checkout")
	public String checkout(Model model){
		return "checkout";
	}

}
