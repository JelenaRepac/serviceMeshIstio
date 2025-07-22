package com.airline.pricingservice.model;

import com.airlines.airlinesharedmodule.Reservation;
import com.airlines.airlinesharedmodule.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(schema = "payment", name = "payment")
@Entity
public class Payment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String userEmail;

        private double amount; // In cents

        private String currency;

        private String status; // e.g., SUCCESS, FAILED, PENDING

        private String paymentProvider; // e.g., Stripe, PayPal

        private String providerSessionId; // Stripe session ID

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        @Column(name = "user_id")
        private Long userId;

        @Column(name = "reservation_id", nullable = false)
        private Long reservationId;



}
