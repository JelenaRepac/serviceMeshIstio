package com.airline.pricingservice.controller;


import com.airline.pricingservice.common.MailType;
import com.airline.pricingservice.model.CheckoutRequest;
import com.airline.pricingservice.service.PaymentService;
import com.airline.pricingservice.service.RemoteEmailSenderService;
import com.stripe.model.checkout.Session;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final RemoteEmailSenderService remoteEmailSenderService;

    @Autowired
    public PaymentController(PaymentService paymentService, RemoteEmailSenderService remoteEmailSenderService) {
        this.paymentService = paymentService;
        this.remoteEmailSenderService = remoteEmailSenderService;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody CheckoutRequest request,
                                                                     @RequestHeader("Authorization") String authHeader) throws StripeException {
        Map<String, String> response = paymentService.createCheckoutSession(request, authHeader);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/send-confirmation-mail", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmail(@RequestParam("email") String email ,
                                    HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        remoteEmailSenderService.sendEmail(email, token, MailType.PAYMENT_SUCCESSFULLY);

        return ResponseEntity.ok("Email send request submitted.");
    }
}
