package com.airline.pricingservice.service;

import com.airline.pricingservice.model.CheckoutRequest;
import com.stripe.exception.StripeException;

import java.util.Map;

public interface PaymentService {
    Map<String, String> createCheckoutSession(CheckoutRequest request, String authHeader) throws StripeException;



}
