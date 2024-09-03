package com.bjcareer.payment.adapter.in.web;

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

import jakarta.servlet.http.HttpServletRequest;
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
	public String createCheckout(Model model) {
		model.addAttribute("cartId", generateCartId());
		model.addAttribute("buyerPk", UUID.randomUUID().toString());
		model.addAttribute("productIds", new int[]{1});
		model.addAttribute("couponIds", new int[]{2});
		return "index";
	}

	@PostMapping("/checkout")
	public Mono<String> checkout(@RequestBody CheckoutRequest checkoutRequest, RedirectAttributes redirectAttributes, HttpServletRequest httpRequest) {
		checkoutRequest.setBuyerPk(httpRequest.getSession().getId()); 		    //임시로 세션아이디를 할당함
		CheckoutCommand checkoutCommand = createCheckoutCommand(checkoutRequest);
		return checkoutUsecase.checkout(checkoutCommand)
			.map(this::redirectToCheckout)
			.doOnError(error -> {
				log.error("Checkout process failed: {}", error.getMessage());
				redirectAttributes.addFlashAttribute("error", "Checkout failed. Please try again.");
			});
	}


	@GetMapping("/redir-checkout")
	public String redirectToCheckoutPage(Model model) {
		return "checkout";
	}

	private String redirectToCheckout(CheckoutResult checkoutResult) {
		return "redirect:/payment/redir-checkout";
	}

	private CheckoutCommand createCheckoutCommand(CheckoutRequest checkoutRequest) {
		return new CheckoutCommand(
			checkoutRequest.getCartId(),
			checkoutRequest.getBuyerPk(),
			checkoutRequest.getProductIds(),
			checkoutRequest.getCouponIds()
		);
	}

	private int generateCartId() {
		return (int) (Math.random() * 1_000_000); // 0부터 999,999까지의 랜덤 숫자
	}
}

