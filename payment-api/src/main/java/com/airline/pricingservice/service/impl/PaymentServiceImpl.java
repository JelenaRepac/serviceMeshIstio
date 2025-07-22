package com.airline.pricingservice.service.impl;

import com.airline.pricingservice.common.PaymentStatus;
import com.airline.pricingservice.model.CheckoutRequest;
import com.airline.pricingservice.model.Payment;
import com.airline.pricingservice.repository.PaymentRepository;
import com.airline.pricingservice.service.PaymentService;
import com.airlines.airlinesharedmodule.Reservation;
import com.airlines.airlinesharedmodule.User;
import com.airlines.airlinesharedmodule.Voucher;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    private final PaymentRepository paymentRepository;

    private final WebClient webClient;

    public PaymentServiceImpl(PaymentRepository paymentRepository, WebClient webClient) {
        this.paymentRepository = paymentRepository;
        this.webClient = webClient;
    }

    @Override
    public Map<String, String> createCheckoutSession(CheckoutRequest request, String authHeader) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/payment-success")
                .setCancelUrl("http://localhost:4200/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount((long) (request.getAmount() * 100)) // in cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Let")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();


        Session session = Session.create(params);


        // get user i reservation for saving payment

        Reservation reservation = webClient.get()
                .uri("http://localhost:9000/api/reservation/{id}", request.getReservationId())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(Reservation.class)
                .block();

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        User user = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8000)
                                .path("/api/auth/profile")
                                .queryParam("email", request.getEmail())
                                .build())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(User.class)
                .block();

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // UKOLIKO POSTOJI VAUCHER
        if (reservation.getVoucherId() != null && !reservation.getVoucherId().isBlank()) {
            Voucher redeemedVoucher = redeemVoucher(reservation.getVoucherId() );

            double discount = request.getAmount() * (redeemedVoucher.getDiscountPercentage() / 100);
            double discountedAmount = request.getAmount() - discount;

            request.setAmount(discountedAmount);
        }

        // kreiraj payment pending status
        Payment payment= Payment.builder()
                .paymentProvider("Stripe")
                .createdAt(LocalDateTime.now())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING.name())
                .providerSessionId(session.getId())
                .updatedAt(LocalDateTime.now())
                .userEmail(request.getEmail())
                .reservationId(request.getReservationId())
                .userId(user.getId())
                .currency(request.getCurrency())
                .build();

        paymentRepository.save(payment);

        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        return response;
    }

    public Voucher redeemVoucher(String code) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/voucher/redeem")
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .bodyToMono(Voucher.class)
                .block();
    }



}
