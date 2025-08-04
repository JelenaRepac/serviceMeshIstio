package com.airline.pricingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private double amount; // iznos u evrima
    private String email;
    private List<Long> reservationId;
    private String currency;

}
