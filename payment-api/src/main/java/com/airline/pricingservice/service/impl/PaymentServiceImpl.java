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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${reservation-service.url}")
    private String reservationServiceUrl;

    @Value("${auth-service.url}")
    private String authServiceUrl;
    @Value("${voucher-service.url}")
    private String voucherServiceUrl;

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

        List<Reservation> reservations = request.getReservationId().stream()
                .map(id -> webClient.get()
                        .uri(reservationServiceUrl+"/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .retrieve()
                        .bodyToMono(Reservation.class)
                        .block())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (reservations.isEmpty()) {
            throw new IllegalArgumentException("No valid reservations found");
        }

        double totalAmount = request.getAmount();

        User user = webClient.get()
                .uri(authServiceUrl + "/profile?email=" + request.getEmail())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(User.class)
                .block();



        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // UKOLIKO POSTOJI VAUCHER
        for (Reservation reservation : reservations) {
            if (reservation.getVoucherId() != null && !reservation.getVoucherId().isBlank()) {
                Voucher voucher = redeemVoucher(reservation.getVoucherId());
                double discount = totalAmount * (voucher.getDiscountPercentage() / 100);
                totalAmount -= discount;
            }
        }

        // kreiraj payment pending status
        for (Long reservation : request.getReservationId()) {
            Payment payment = Payment.builder()
                    .paymentProvider("Stripe")
                    .createdAt(LocalDateTime.now())
                    .amount(totalAmount)
                    .status(PaymentStatus.PENDING.name())
                    .providerSessionId(session.getId())
                    .updatedAt(LocalDateTime.now())
                    .userEmail(request.getEmail())
                    .reservationId(reservation)
                    .userId(user.getId())
                    .currency(request.getCurrency())
                    .build();

            paymentRepository.save(payment);
        }


        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        return response;
    }

    public Voucher redeemVoucher(String code) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(voucherServiceUrl+"/redeem")
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .bodyToMono(Voucher.class)
                .block();
    }



}
